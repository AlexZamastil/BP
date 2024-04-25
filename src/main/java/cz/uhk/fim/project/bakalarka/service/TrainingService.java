package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.DAO.*;
import cz.uhk.fim.project.bakalarka.DTO.DayDTO;
import cz.uhk.fim.project.bakalarka.model.*;
import cz.uhk.fim.project.bakalarka.DTO.trainingDTO;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.*;
import weka.core.converters.ConverterUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Service class responsible for handling training-related operations.
 *
 * @author Alex Zamastil
 */
@Service
@Log4j2
public class TrainingService {
    @Value("${filepath.gladiator:src/main/resources/files}")
    public String filePath;
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
    private Classifier classifier;
    private Instances data;

    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;
    private final DayRepository dayRepository;
    private final FoodRepository foodRepository;
    private final GymWorkoutRepository gymWorkoutRepository;
    private final RunRepository runRepository;
    private final SwimmingRepository swimmingRepository;
    private final MessageSource messageSource;
    private final UserStatsRepository userStatsRepository;
    MessageHandler<String> messageHandler = new MessageHandler<>();
    public final double MAX_PERCENTAGE_INCREASE_LENGTH_PER_TRAINING_DAY = 1.3;
    public final double MAX_PERCENTAGE_INCREASE_PACE_PER_TRAINING_DAY = 1;
    public final double LONG_RUN_THRESHOLD = 12000;
    public static int MAX_CALORIC_DEVIATION = 400;
    public static int MIN_RUN_LENGTH = 2000;
    public static int MAX_RUN_LENGTH = 21000;


    public TrainingService(UserRepository userRepository, TrainingRepository trainingRepository, DayRepository dayRepository, FoodRepository foodRepository, GymWorkoutRepository gymWorkoutRepository, RunRepository runRepository, SwimmingRepository swimmingRepository, UserStatsRepository userStatsRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
        this.dayRepository = dayRepository;
        this.foodRepository = foodRepository;
        this.gymWorkoutRepository = gymWorkoutRepository;
        this.runRepository = runRepository;
        this.swimmingRepository = swimmingRepository;
        this.userStatsRepository = userStatsRepository;
        this.messageSource = messageSource;
    }

    /**
     * Generates a training play based on provided parameters.
     * Validates provided data to determine if the training makes sense.
     * Create instances of Training and Day object, while utilizing J48 algorithm.
     *
     * @param trainingData DTO containing all the necessary data to create a training plan.
     * @param token        Users JWToken.
     * @return indicating the success or failure of the training plan generation process.
     */
    @Transactional
    public ResponseEntity<?> generateTraining(trainingDTO trainingData, String token) {
        if (StringUtils.isBlank(trainingData.getElevationProfile().toString()) ||
                trainingData.getLengthOfRaceInMeters() == null ||
                StringUtils.isBlank(trainingData.getLengthOfRaceInMeters().toString()) ||
                trainingData.getLengthOfRaceInMeters() == 0
        ) {
            return messageHandler.error(messageSource.getMessage("error.training.null", null, LocaleContextHolder.getLocale()));
        }

        int trainingDays = (int) ChronoUnit.DAYS.between(trainingData.getStartDay(), trainingData.getRaceDay());
        log.info("training days: " + trainingDays);
        if (trainingData.getRaceDay().isBefore(LocalDate.now()) || trainingDays < 30 || trainingDays > 365)
            return messageHandler.error(messageSource.getMessage("error.training.invalidTime", null, LocaleContextHolder.getLocale()));

        int trainingDaysPerWeek = trainingData.countTrainingDays();

        log.info(trainingData);
        User user = userRepository.findUserByToken(token);
        UserStats userStats = userStatsRepository.findUserStatsByUser(user);


        ResponseEntity<?> testTraining = testTrainingPossibility(
                trainingDays,
                trainingData.getLengthOfRaceInMeters(),
                trainingData.getWantedPace(),
                userStats.getAverageRunLength(),
                userStats.getAverageRunPace(),
                user,
                trainingDaysPerWeek
        );
        log.info(testTraining.getStatusCode());
        if (testTraining.getStatusCode() == HttpStatusCode.valueOf(400)) {
            return messageHandler.error(Objects.requireNonNull(testTraining.getBody()).toString());
        }

        List<ExerciseType> exercisePool = new ArrayList<>();
        List<Food> foodPool = foodRepository.findAll();
        String predictedCaloryProgram = "";

        //for OCR the training consists of all 3 types of exercise to ensure versatility
        ConverterUtils.DataSource gladiator;
        String finalDatasetPath = filePath + "/final_dataset.arff";

        try {
            gladiator = new ConverterUtils.DataSource(finalDatasetPath);
            Instances data = gladiator.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);
            this.data = data;
            J48 j48 = new J48();
            j48.setUnpruned(false);
            //j48.setUseLaplace(true);// does not make a difference
            //String[] options = {"-C", "0.25"}; does not make a difference
            //j48.setOptions(options);
            j48.buildClassifier(data);
            //dataset evaluation
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(j48, data, 2, new java.util.Random(1));
            log.info(eval.toSummaryString());
            classifier = j48;
        } catch (Exception e) {
            log.error(e);
            return messageHandler.error(messageSource.getMessage("error.training.read", null, LocaleContextHolder.getLocale()));
        }

        //Data initialization
        String BMI;
        if (userStats.getBmi() < 18.5) {
            BMI = "UNDERWEIGHT";
        } else if (userStats.getBmi() < 25) {
            BMI = "NORMAL";
        } else if (userStats.getBmi() < 30) {
            BMI = "OVERWEIGHT";
        } else BMI = "OBESE";

        double increase = roundNumber(trainingData.getWantedPace());

        //inserting values as input for the algorithm
        try {
            Instance instance = new DenseInstance(4);
            instance.setDataset(data);
            instance.setValue(0, data.attribute("bodyType").indexOfValue(userStats.getUser().getBodyType().toString()));
            instance.setValue(1, data.attribute("BMI").indexOfValue(BMI));
            instance.setValue(2, data.attribute("percentageIncrease").indexOfValue(String.valueOf(increase)));

            data.setClassIndex(3);
            //Calculating probabilities of each possibility and returning the highest
            double[] predictions = classifier.distributionForInstance(instance);
            double maxProbability = -1;
            int predictedClass = -1;
            for (int i = 0; i < predictions.length; i++) {
                if (predictions[i] > maxProbability) {
                    maxProbability = predictions[i];
                    predictedClass = i;
                }
            }
            //final value of J48 decision tree:
            predictedCaloryProgram = data.classAttribute().value(predictedClass);

            log.info("BodyType: " + userStats.getUser().getBodyType().toString());
            log.info("BMI: " + BMI);
            log.info("Percentage Increase: " + increase);
            log.info("Predicted Training Algorithm: " + predictedCaloryProgram);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (trainingData.getType().toString().equals("OCR")) {
            List<GymWorkout> gymWorkouts = gymWorkoutRepository.findAll();
            List<Run> runs = runRepository.findAll();
            exercisePool.addAll(runs);
            exercisePool.addAll(gymWorkouts);
        }

        if (trainingData.getType().toString().equals("RUN") && trainingData.getLengthOfRaceInMeters() > LONG_RUN_THRESHOLD && trainingDays > 90) {
            //for long run the training prioritize only running and swimming for endurance and VO2MAX
            List<Run> runs = runRepository.findAll();
            List<Swimming> swimming = swimmingRepository.findAll();
            exercisePool.addAll(runs);
            exercisePool.addAll(swimming);
        }   //for classic form of training the training focuses only on running. This training should be for everyone and not so hard challenge
        else {
            List<Run> runs = runRepository.findAll();
            exercisePool.addAll(runs);
        }

        Training training = new Training(
                trainingData.getRaceDay(),
                trainingData.getType(),
                trainingData.getStartDay(),
                trainingData.getLengthOfRaceInMeters(),
                user);
        trainingRepository.save(training);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        //basal metabolic rate of user
        double BMR = userStats.getBmr();
        double caloricGain = 0;
        double activeCaloricBurn = 0;
        switch (predictedCaloryProgram) {
            case "HIGH_GAIN_HIGH_BURN" -> {
                //maintaining weight with hard training
                caloricGain = BMR * 1.5;
                activeCaloricBurn = caloricGain - BMR;
            }
            case "LOW_GAIN_HIGH_BURN" -> {
                // deficit
                caloricGain = BMR * 1.2;
                activeCaloricBurn = caloricGain - BMR;
            }
            case "HIGH_GAIN_LOW_BURN" -> {
                //surplus
                caloricGain = BMR * 1.5;
                activeCaloricBurn = caloricGain - 250 - BMR;
            }
            case "LOW_GAIN_LOW_BURN" -> {
                //maintaining weight on diet a light training
                caloricGain = BMR * 1.2;
                activeCaloricBurn = caloricGain - 250 - BMR;
            }
        }
        String lastExercise = "RUN";
        for (int x = 0; x < trainingDays; x++) {
            List<ExerciseType> exercises = new ArrayList<>(exercisePool);
            List<Food> food = new ArrayList<>(foodPool);
            log.info(calendar.getTime());
            log.info("Day of the week: " + sdf.format(calendar.getTime()));
            Day day = new Day();
            day.setDate(calendar.getTime());
            day.setTraining(training);
            day.setUser(user);
            //day.setCaloriesBurned(userStats.getBmr());

            if (isTrainingDay(calendar.get(Calendar.DAY_OF_WEEK), trainingData)) {
                lastExercise = generateExercises(day, exercises, activeCaloricBurn, lastExercise, trainingData);
            }

            generateFood(day, food, caloricGain);
            dayRepository.save(day);
            calendar.add(Calendar.DATE, 1);
        }


        return messageHandler.success(messageSource.getMessage("success.training.generated", null, LocaleContextHolder.getLocale()));
    }

    public String generateExercises(Day day, List<ExerciseType> exercisePool, double caloricBurn, String lastExercise, trainingDTO trainingData) {
        if(!trainingData.getType().toString().equals("RUN")){
            exercisePool.removeIf(e -> e.getType().equals(lastExercise));
        }

        log.info("exercise pool: " + exercisePool.size());
        if (exercisePool.size() < 3) {
            log.info("Insufficient exercises available.");

            return null;
        }


        Set<Exercise> exerciseList = new HashSet<>();
        double calories = 0;
        Collections.shuffle(exercisePool);
        log.info("Exercise pool: ");
        for (ExerciseType e : exercisePool ){
            log.info(e.getExercise().getName());
        }
        int i = 0;
        while (calories < (caloricBurn + MAX_CALORIC_DEVIATION) && i < exercisePool.size() - 1) {
            exerciseList.add(exercisePool.get(i).getExercise());
            day.setCaloriesBurned(day.getCaloriesBurned() + exercisePool.get(i).getExercise().getCaloriesBurned());
            if (exercisePool.get(i).getType().equals("GYM")){
                //gym workout is usually hard and burns way less calories that run or swimming,
                // the caloric output of this exercise is lower, usually between 300 and 800 calories burned, big portion of the burned calories are from cardio
                calories += 2*(exercisePool.get(i).getExercise().getCaloriesBurned());
            } else {
                calories += exercisePool.get(i).getExercise().getCaloriesBurned();
            }

            i++;
        }
        log.info("Exercises being added: ");
        log.info(exerciseList);
        for (Exercise e : exerciseList ){
            log.info(e.getName());
        }
        day.setExercises(exerciseList);
        return exercisePool.get(0).getType();
    }

    public void generateFood(Day day, List<Food> foodPool, double caloricGain) {
        log.info("food pool: " + foodPool.size());
        if (foodPool.size() < 5) {
            log.info("Insufficient food available.");
            return;
        }
        Set<Food> foodList = new HashSet<>();
        double calories = 0;
        Collections.shuffle(foodPool);
        int i = 0;
        log.info(caloricGain);
        while (calories < (caloricGain - MAX_CALORIC_DEVIATION) && i < foodPool.size()) {
            foodList.add(foodPool.get(i));
            day.setCaloriesGained(day.getCaloriesGained() + foodPool.get(i).getCaloriesGained());
            calories += foodPool.get(i).getCaloriesGained();
            log.info(calories);
            i++;
        }
        day.setMenu(foodList);
    }

    /**
     * Determines if the day of the week is a training day
     *
     * @param dayOfWeek Day of the week
     * @param c         DTO containing the training day
     * @return true if the day is training day, otherwise false
     */

    public boolean isTrainingDay(int dayOfWeek, trainingDTO c) {
        return switch (dayOfWeek) {
            case Calendar.MONDAY -> c.isMonday();
            case Calendar.TUESDAY -> c.isTuesday();
            case Calendar.WEDNESDAY -> c.isWednesday();
            case Calendar.THURSDAY -> c.isThursday();
            case Calendar.FRIDAY -> c.isFriday();
            case Calendar.SATURDAY -> c.isSaturday();
            case Calendar.SUNDAY -> c.isSunday();
            default -> false;
        };
    }

    public double roundNumber(double number) {
        if (number > 5.5) return 5.5;
        return Math.round(number * 2) / 2.0;
    }

    /**
     * Tests the possibility of a training plan based on the provided parameters.
     *
     * @param numberOfDays     The number of days available for training.
     * @param raceLength       The length of the race.
     * @param wantedPace       The desired pace for the race.
     * @param averageRunLength The average length of the user's runs.
     * @param averagePace      The average pace of the user's runs.
     * @param user             The user for whom the training plan is being tested.
     * @return ResponseEntity indicating whether the training plan is feasible or not.
     */

    public ResponseEntity<?> testTrainingPossibility(int numberOfDays,
                                                     Integer raceLength,
                                                     Double wantedPace,
                                                     Integer averageRunLength,
                                                     double averagePace,
                                                     User user,
                                                     int trainingDaysPerWeek
    ) {
        if (!isGoalSuitable(raceLength)) {
            return messageHandler.error(messageSource.getMessage("error.training.badRace", null, LocaleContextHolder.getLocale()));
        }

        double numberOfWeeks = numberOfDays / 7.0;

        log.info("Weeks: " + numberOfWeeks);
        log.info("Average length: " + averageRunLength);
        log.info("Race length: " + raceLength);
        if (averageRunLength < raceLength && raceLength < MAX_RUN_LENGTH) {
            // for long runs we cannot compare average training length, because it is not usual to run the race length as a training
            double x = weeklyPercentageIncreaseInLength(numberOfWeeks, averageRunLength, raceLength);
            log.info("Weekly percentage increase in length: " + x);
            if (x > (MAX_PERCENTAGE_INCREASE_LENGTH_PER_TRAINING_DAY) * trainingDaysPerWeek)
                return messageHandler.error(messageSource.getMessage("error.training.badLength", null, LocaleContextHolder.getLocale()));
        }
        log.info("Average pace: " + averagePace);
        log.info("Wanted pace: " + wantedPace);

        if (wantedPace != null && averagePace > wantedPace) {

            double x = weeklyPercentageIncreaseInPace(numberOfWeeks, averagePace, wantedPace);
            log.info("Weeks: " + numberOfWeeks);
            log.info("Weekly percentage increase in pace: " + x);
            if (x > (MAX_PERCENTAGE_INCREASE_PACE_PER_TRAINING_DAY) * trainingDaysPerWeek)
                return messageHandler.error(messageSource.getMessage("error.training.badPace", null, LocaleContextHolder.getLocale()));
        }

        return ResponseEntity.ok("ok");
    }

    /**
     * Calculates the weekly percentage increase in run length based on the provided parameters.
     *
     * @param numberOfWeeks    The number of weeks in the training period.
     * @param averageRunLength The average length of the user's runs.
     * @param raceLength       The length of the race.
     * @return The weekly percentage increase in run length.
     */
    public double weeklyPercentageIncreaseInLength(double numberOfWeeks, int averageRunLength, int raceLength) {
        int metersIncrease = raceLength - averageRunLength;
        double weeklyIncrease = metersIncrease / numberOfWeeks;
        return weeklyIncrease / averageRunLength * 100;
    }

    /**
     * Calculates the weekly percentage increase in run pace based on the provided parameters.
     *
     * @param numberOfWeeks The number of weeks in the training period.
     * @param averagePace   The average pace of the user's runs.
     * @param wantedPace    The desired pace for the race.
     * @return The weekly percentage increase in run pace.
     */
    public double weeklyPercentageIncreaseInPace(double numberOfWeeks, double averagePace, double wantedPace) {
        double paceIncrease = averagePace - wantedPace;//the lower the pace is, the faster is run
        double weeklyIncrease = paceIncrease / numberOfWeeks;
        return weeklyIncrease / averagePace * 100;
    }

    /**
     * Determines if the specified race length is suitable for training.
     *
     * @param lengthOfRaceInMeters The length of the race in meters.
     * @return true if the race length is between 1000 and 42195 meters, false otherwise.
     */
    public Boolean isGoalSuitable(Integer lengthOfRaceInMeters) {
        return lengthOfRaceInMeters >= MIN_RUN_LENGTH && lengthOfRaceInMeters <= MAX_RUN_LENGTH;
    }

    /**
     * Checks if the user with the specified ID has an active training session.
     *
     * @param id The ID of the user.
     * @return ResponseEntity indicating whether the user has an active training session or not.
     */
    public ResponseEntity<?> hasActiveTraining(long id) {
        LocalDate today = LocalDate.now();
        List<Training> trainings = trainingRepository.findTrainingsContainingUser(id);
        for (Training t : trainings
        ) {
            if (today.isBefore(t.getRaceday()))
                return ResponseEntity.ok(t.getId());
        }
        return ResponseEntity.ok("no");
    }

    /**
     * Retrieves the active training for the user with the specified ID.
     * The training consists of a list of Day entity.
     *
     * @param id The ID of the user.
     * @return ResponseEntity containing the active training (list of Day entity) or an error message if not found.
     */
    public ResponseEntity<?> getActiveTraining(long id) {
        User user = userRepository.findUserById(id);
        Training activeTraining = trainingRepository.findTrainingByUserAndRacedayIsAfter(user, LocalDate.now());
        log.info(activeTraining);
        if (activeTraining == null)
            return messageHandler.error(messageSource.getMessage("error.training.notFound", null, LocaleContextHolder.getLocale()));
        List<Day> trainingDays = dayRepository.findUpcomingTrainingDays(activeTraining);
        List<DayDTO> daysDTO = new ArrayList<>();
        for (Day d : trainingDays) {
            DayDTO dayDTO = new DayDTO(d.getDate(), d.getCaloriesGained(), d.getCaloriesBurned(), d.getExercises(), d.getMenu());
            daysDTO.add(dayDTO);
        }
        trainingDTO trainingDTO = new trainingDTO();
        trainingDTO.setLengthOfRaceInMeters(activeTraining.getLengthinmeters());
        trainingDTO.setType(activeTraining.getType());
        trainingDTO.setStartDay(activeTraining.getStartday());
        trainingDTO.setRaceDay(activeTraining.getRaceday());

        int days = (int) ChronoUnit.DAYS.between(activeTraining.getStartday(), activeTraining.getRaceday());
        int daysProgress = (int) ChronoUnit.DAYS.between(activeTraining.getStartday(), LocalDate.now());
        Map<String, Object> response = new HashMap<>();
        response.put("trainingDays", daysDTO);
        response.put("trainingInfo", trainingDTO);
        response.put("daysTotal", days);
        response.put("daysSoFar", daysProgress);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes the training with the specified ID.
     *
     * @param id The ID of the training to be deleted.
     * @return ResponseEntity indicating the success or failure of the deletion operation.
     */
    public ResponseEntity<?> deleteTraining(long id) {
        Optional<Training> t = trainingRepository.findById(id);

        if (t.isPresent()) {
            trainingRepository.deleteTrainingWithData(t.get());
            return messageHandler.success(messageSource.getMessage("success.onDelete", null, LocaleContextHolder.getLocale()));
        } else {
            return messageHandler.error(messageSource.getMessage("error.dbs.onDelete", null, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Retrieves the list of trainings associated with the user obtained by the token.
     *
     * @param token The authentication token of the user
     * @return ResponseEntity containing the list of trainings or an error message if the user is not found.
     */
    public ResponseEntity<?> getTrainings(String token) {
        User user = userRepository.findUserByToken(token);
        if (user == null)
            return messageHandler.error(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
        List<Training> trainings = trainingRepository.findTrainingsContainingUser(user.getId());
        if (trainings.isEmpty())
            return messageHandler.error(messageSource.getMessage("error.training.nullFound", null, LocaleContextHolder.getLocale()));

        Map<String, Object> response = new HashMap<>();

        for (Training t : trainings) {

            trainingDTO trainingDTO = new trainingDTO();
            trainingDTO.setStartDay(t.getStartday());
            trainingDTO.setRaceDay(t.getRaceday());
            trainingDTO.setType(t.getType());
            trainingDTO.setLengthOfRaceInMeters(t.getLengthinmeters());

            response.put(String.valueOf(t.getId()), trainingDTO);
        }
        return ResponseEntity.ok(response);
    }
}
