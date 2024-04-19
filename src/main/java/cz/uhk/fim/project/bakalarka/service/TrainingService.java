package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.DAO.*;
import cz.uhk.fim.project.bakalarka.DTO.DayDTO;
import cz.uhk.fim.project.bakalarka.model.*;
import cz.uhk.fim.project.bakalarka.DTO.CreateTrainingDTO;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
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

    public String dataset = "src/main/resources/files/dataset_1.arff";
    public String gladiatorData = "src/main/resources/files/gladiator_data.arff";
    public String runData = "src/main/resources/files/run_data.arff";
    public String longRunData = "src/main/resources/files/long_run_data.arff";
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
    MessageHandler<List<Training>> trainingListMessageHandler = new MessageHandler<>();
    public final double MAX_PERCENTAGE_INCREASE_LENGTH_PER_TRAINING_DAY = 1.3;
    public final double MAX_PERCENTAGE_INCREASE_PACE_PER_TRAINING_DAY = 0.8;
    public final double LONG_RUN_THRESHOLD = 12000;


    public TrainingService(UserRepository userRepository, TrainingRepository trainingRepository, DayRepository dayRepository, ExerciseRepository exerciseRepository, FoodRepository foodRepository, GymWorkoutRepository gymWorkoutRepository, RunRepository runRepository, SwimmingRepository swimmingRepository, UserStatsRepository userStatsRepository, MessageSource messageSource) {
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
     * Trains the model using a dataset.
     *
     * @throws Exception If an error occurs during model training.
     */
    public void trainModel() throws Exception {
        ConverterUtils.DataSource dataSource = new ConverterUtils.DataSource(gladiatorData);
        Instances data = dataSource.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);

        this.data = data;
        J48 j48 = new J48();
        j48.buildClassifier(data);

        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(j48, data, 10, new java.util.Random(1));
        log.info(eval.toSummaryString());
        classifier = j48;
    }

    /**
     * Generates a training play based on provided parameters.
     * Validates provided data to determine if the training makes sense.
     * Create instances of Training and Day object, while utilizing J48 algorithm.
     *
     * @param trainingData DTO containing all the necessary data to create a training plan.
     * @param token        Users JWToken.
     * @returnResponseEntity indicating the success or failure of the training plan generation process.
     */
    @Transactional
    public ResponseEntity<?> generateTraining(CreateTrainingDTO trainingData, String token) {
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

        if (trainingData.getType().toString().equals("OCR")) {
            //for OCR the training consists of all 3 types of exercise to ensure versatility
            ConverterUtils.DataSource gladiator;
            try {
                gladiator = new ConverterUtils.DataSource(gladiatorData);
                Instances data = gladiator.getDataSet();
                data.setClassIndex(data.numAttributes() - 1);

                J48 j48 = new J48();
                //j48.setUnpruned(false);
                //j48.setUseLaplace(true);
                //String[] options = {"-U"};
                //j48.setOptions(options);
                j48.buildClassifier(data);
                //dataset evaluation
                Evaluation eval = new Evaluation(data);
                eval.crossValidateModel(j48, data, 3, new java.util.Random(1));
                log.info(eval.toSummaryString());
                classifier = j48;
            } catch (Exception e) {
                log.error(e);
                return messageHandler.error(messageSource.getMessage("error.training.read", null, LocaleContextHolder.getLocale()));
            }


            List<GymWorkout> gymWorkouts = gymWorkoutRepository.findAll();
            List<Run> runs = runRepository.findAll();
            List<Swimming> swimming = swimmingRepository.findAll();
            exercisePool.addAll(runs);
            exercisePool.addAll(gymWorkouts);
            exercisePool.addAll(swimming);

        }

        if (trainingData.getType().toString().equals("RUN") && trainingData.getLengthOfRaceInMeters() < LONG_RUN_THRESHOLD && trainingDays > 90) {
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

        for (int x = 0; x < trainingDays; x++) {
            List<ExerciseType> exercises = new ArrayList<>(exercisePool);
            List<Food> food = new ArrayList<>(foodPool);
            log.info(calendar.getTime());
            log.info("Day of the week: " + sdf.format(calendar.getTime()));
            Day day = new Day();
            day.setDate(calendar.getTime());
            day.setTraining(training);
            day.setUser(user);
            day.setCaloriesGained(userStats.getBmr());
            if(isTrainingDay(calendar.get(Calendar.DAY_OF_WEEK),trainingData)){
                generateExercises(userStats, day, exercises,trainingData);
            }
            generateFood(userStats, day, food);
            dayRepository.save(day);
            calendar.add(Calendar.DATE, 1);
        }


        return messageHandler.success(messageSource.getMessage("success.training.generated", null, LocaleContextHolder.getLocale()));
    }

    public void generateExercises(UserStats userStats, Day day, List<ExerciseType> exercises, CreateTrainingDTO trainingData) {
        log.info("exercise pool: " + exercises.size());
        if (exercises.size() < 3) {
            log.info("Insufficient exercises available.");

            return;
        }
        Random random = new Random();
        Set<Exercise> exerciseList = new HashSet<>();
        while (exerciseList.size() < 3) {
            int randomIndex = random.nextInt(exercises.size());
            Exercise randomExercise = exercises.get(randomIndex).getExercise();
            exerciseList.add(randomExercise);
            day.setCaloriesBurned(day.getCaloriesBurned());
            exercises.remove(randomIndex);
        }
        day.setExercises(exerciseList);
    }

    public void generateFood(UserStats userStats, Day day, List<Food> foodPool) {
        log.info("food pool: " + foodPool.size());
        if (foodPool.size() < 3) {
            log.info("Insufficient food available.");

            return;
        }

        Random random = new Random();
        Set<Food> foodList = new HashSet<>();
        while (foodList.size() < 3) {
            int randomIndex = random.nextInt(foodPool.size());
            Food randomFood = foodPool.get(randomIndex);
            foodList.add(randomFood);
            day.setCaloriesGained(day.getCaloriesGained()+randomFood.getCaloriesGained());
            foodPool.remove(randomIndex);
        }
        day.setMenu(foodList);
    }

    /**
     * Determines if the day of the week is a training day
     *
     * @param dayOfWeek Day of the week
     * @param c DTO containing the training day
     * @return true if the day is training day, otherwise false
     */

    public boolean isTrainingDay(int dayOfWeek, CreateTrainingDTO c) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return c.isMonday();
            case Calendar.TUESDAY:
                return c.isTuesday();
            case Calendar.WEDNESDAY:
                return c.isWednesday();
            case Calendar.THURSDAY:
                return c.isThursday();
            case Calendar.FRIDAY:
                return c.isFriday();
            case Calendar.SATURDAY:
                return c.isSaturday();
            case Calendar.SUNDAY:
                return c.isSunday();
            default:
                return false;
        }


    }

    /**
     * Tests the possibility of a training plan based on the provided parameters.
     *
     * @param numberOfDays       The number of days available for training.
     * @param raceLength         The length of the race.
     * @param wantedPace         The desired pace for the race.
     * @param averageRunLength   The average length of the user's runs.
     * @param averagePace        The average pace of the user's runs.
     * @param user               The user for whom the training plan is being tested.
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
        if (averageRunLength < raceLength && raceLength < 20000) {
            // for long runs we cannot compare average training length, because it is not usual to run the race length as a training
            double x = weeklyPercentageIncreaseInLength(numberOfWeeks, averageRunLength, raceLength);
            log.info("Weekly percentage increase in length: " + x);
            if (x > (MAX_PERCENTAGE_INCREASE_LENGTH_PER_TRAINING_DAY)*trainingDaysPerWeek)
                return messageHandler.error(messageSource.getMessage("error.training.badLength", null, LocaleContextHolder.getLocale()));
        }
        log.info("Average pace: " + averagePace);
        log.info("Wanted pace: " + wantedPace);

        if (wantedPace != null && averagePace > wantedPace ) {

            double x = weeklyPercentageIncreaseInPace(numberOfWeeks, averagePace, wantedPace);
            log.info("Weeks: " + numberOfWeeks);
            log.info("Weekly percentage increase in pace: " + x);
            if (x > (MAX_PERCENTAGE_INCREASE_PACE_PER_TRAINING_DAY)*trainingDaysPerWeek)
                return messageHandler.error(messageSource.getMessage("error.training.badPace", null, LocaleContextHolder.getLocale()));
        }

        if (raceLength > 20000 && (
                (user.getBodyType().toString().equals("OBESE") && numberOfWeeks < 20) ||
                        (user.getBodyType().toString().equals("AVERAGE") && numberOfWeeks < 16) ||
                        (user.getBodyType().toString().equals("ATHLETIC") && numberOfWeeks < 12))) {
            return messageHandler.error(messageSource.getMessage("error.training.longRace", null, LocaleContextHolder.getLocale()));
        }
        return ResponseEntity.ok("ok");
    }
    /**
     * Calculates the weekly percentage increase in run length based on the provided parameters.
     *
     * @param numberOfWeeks     The number of weeks in the training period.
     * @param averageRunLength  The average length of the user's runs.
     * @param raceLength        The length of the race.
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
     * @param averagePace    The average pace of the user's runs.
     * @param wantedPace     The desired pace for the race.
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
        return lengthOfRaceInMeters >= 1000 && lengthOfRaceInMeters <= 42195;
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
        CreateTrainingDTO createTrainingDTO = new CreateTrainingDTO();
        createTrainingDTO.setLengthOfRaceInMeters(activeTraining.getLengthinmeters());
        createTrainingDTO.setType(activeTraining.getType());
        createTrainingDTO.setStartDay(activeTraining.getStartday());
        createTrainingDTO.setRaceDay(activeTraining.getRaceday());
        Map<String, Object> response = new HashMap<>();
        response.put("trainingDays", daysDTO);
        response.put("trainingInfo", createTrainingDTO);
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
        return trainingListMessageHandler.success(trainings);
    }


    public ResponseEntity<?> generateTrainingPlan(User user,
                                                  int availableTrainingDays,
                                                  int trainingLengthInDays,
                                                  int averageRunLength,
                                                  double trainingPace,
                                                  int raceLength,
                                                  double racePaceToAchieve,
                                                  String raceType,
                                                  String raceElevation
    ) {

    /*    try {
            int[] attributeIndices = {13, 14};
            for (int attributeIndex : attributeIndices) {

                Instance instance = new DenseInstance(16);

                instance.setDataset(data);

                instance.setValue(0, weight);
                instance.setValue(1, height);
                instance.setValue(2, bmi);
                instance.setValue(3, sex);
                instance.setValue(4, bodyType);
                instance.setValue(5, availableTrainingDays);
                instance.setValue(6, trainingLengthInDays);
                instance.setValue(7, trainingLengthInMeters);
                instance.setValue(8, trainingPace);
                instance.setValue(9, raceLength);
                instance.setValue(10, racePaceToAchieve);
                instance.setValue(11, raceType);
                instance.setValue(12, raceElevation);

                data.setClassIndex(attributeIndex);

                double[] predictions = classifier.distributionForInstance(instance);

                int type = data.attribute(attributeIndex).type();
                log.info("attribute type is " + type);

                if (type == Attribute.NUMERIC) {
                    // get numeric data
                    double predictedNumericValue = classifier.classifyInstance(instance);
                    log.info("Predicted Numeric Value for Attribute " + attributeIndex + ": " + predictedNumericValue);

                } else if (type == Attribute.NOMINAL) {
                    // get nominal data

                    int predictedClassIndex = -1;
                    double maxProbability = Double.NEGATIVE_INFINITY;

                    for (int i = 0; i < predictions.length; i++) {
                        if (predictions[i] > maxProbability) {
                            maxProbability = predictions[i];
                            predictedClassIndex = i;
                        }
                    }

                    String predictedClass = data.attribute(attributeIndex).value(predictedClassIndex);
                    log.info("Predicted Nominal Value for Attribute " + attributeIndex + ": " + predictedClass);
                }
            }
            return ResponseEntity.ok(messageSource.getMessage("success.training.generated", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(messageSource.getMessage("error.training.failed", null, LocaleContextHolder.getLocale()));
        }*/
        return ResponseEntity.ok("ok");
    }
}
