package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.DAO.TrainingRepository;
import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.DAO.UserStatsRepository;
import cz.uhk.fim.project.bakalarka.enumerations.Goal;
import cz.uhk.fim.project.bakalarka.model.Training;
import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.model.UserStats;
import cz.uhk.fim.project.bakalarka.DTO.CreateTrainingDTO;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.*;
import weka.core.converters.ConverterUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static cz.uhk.fim.project.bakalarka.enumerations.Goal.RUN;

@Service
public class TrainingService {

    public String dataset = "src/main/java/cz/uhk/fim/project/bakalarka/files/dataset_1.arff";
    private J48 trainedModel;
    private Classifier classifier;
    private Instances data;
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;
    private final MessageSource messageSource;
    private final UserStatsRepository userStatsRepository;
    public final double MAX_PERCENTAGE_INCREASE = 2.5;

    public TrainingService(UserRepository userRepository, TrainingRepository trainingRepository, UserStatsRepository userStatsRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
        this.userStatsRepository = userStatsRepository;
        this.messageSource = messageSource;
    }

    public void trainModel() throws Exception {
        ConverterUtils.DataSource dataSource = new ConverterUtils.DataSource(dataset);
        Instances data = dataSource.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);

        this.data = data;

        J48 j48 = new J48();
        j48.buildClassifier(data);

        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(j48, data, 10, new java.util.Random(1));
        System.out.println(eval.toSummaryString());
        trainedModel = j48;
        classifier = j48;
    }

    public ResponseEntity<?> generateTraining(CreateTrainingDTO createTrainingRequest, HttpServletRequest httpServletRequest) {
        System.out.println(createTrainingRequest);
        Instant wantedTime = Instant.parse(createTrainingRequest.getWantedTime());
        Instant actualTime = Instant.parse(createTrainingRequest.getActualTime());

        LocalDate today = LocalDate.now();

        Instant startOfDay = today.atStartOfDay(ZoneOffset.UTC).toInstant();

        Duration wantedTimeDuration = Duration.between(startOfDay, wantedTime);
        Duration actualTimeDuration = Duration.between(startOfDay, actualTime);

        System.out.println("WANTED TIME: " + wantedTimeDuration);
        System.out.println("ACTUAL TIME: " + actualTimeDuration);

        String token = httpServletRequest.getHeader("Authorization");
        User user = userRepository.findUserByToken(token);
        UserStats userStats = userStatsRepository.findUserStatsByUser(user);

        float minutesOfActualTime = actualTimeDuration.toMinutes();
        double actualPace = minutesOfActualTime / createTrainingRequest.getActualRunLength();

        float minutesOfWantedTime = wantedTimeDuration.toMinutes();
        double wantedPace = minutesOfWantedTime / createTrainingRequest.getActualRunLength();


        int trainingDays = (int) ChronoUnit.DAYS.between(createTrainingRequest.getStartDay(), createTrainingRequest.getRaceDay());
//temp
        String firstLetter = user.getBodyType().toString().substring(0, 1).toUpperCase();
        String restLetters = user.getBodyType().toString().substring(1).toLowerCase();
        String adjustedBodyType = firstLetter + restLetters;
        String firstLetter1 = createTrainingRequest.getGoal().toString().substring(0, 1).toUpperCase();
        String restLetters1 = createTrainingRequest.getGoal().toString().substring(1).toLowerCase();
        String adjustedRun;
        if (createTrainingRequest.getGoal().toString().equals("RUN"))
            adjustedRun = firstLetter1 + restLetters1;
        else adjustedRun = createTrainingRequest.getGoal().toString();
//

        generateTrainingPlan(
                user.getWeight(),
                user.getHeight(),
                userStats.getBmi(),
                user.getSex().toString(),
                adjustedBodyType,
                1,
                trainingDays,
                createTrainingRequest.getActualRunLength(),
                actualPace,
                createTrainingRequest.getLengthOfRaceInMeters(),
                wantedPace,
                adjustedRun,
                createTrainingRequest.getElevationProfile().toString()
        );

        testTrainingPossibility(
                createTrainingRequest.getStartDay(),
                createTrainingRequest.getRaceDay(),
                createTrainingRequest.getLengthOfRaceInMeters(),
                wantedTimeDuration, createTrainingRequest.getActualRunLength(),
                actualTimeDuration);

        return MessageHandler.success(messageSource.getMessage("success.training.generated", null, LocaleContextHolder.getLocale()));
    }

    public ResponseEntity<?> generateTrainingPlan(double weight,
                                                  double height,
                                                  double bmi,
                                                  String sex,
                                                  String bodyType,
                                                  int availableTrainingDays,
                                                  int trainingLengthInDays,
                                                  int trainingLengthInMeters,
                                                  double trainingPace,
                                                  int raceLength,
                                                  double racePaceToAchieve,
                                                  String raceType,
                                                  String raceElevation
    ) {

        try {
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
                System.out.println("attribute type is " + type);

                if (type == Attribute.NUMERIC) {
                    // get numeric data
                    double predictedNumericValue = classifier.classifyInstance(instance);
                    System.out.println("Predicted Numeric Value for Attribute " + attributeIndex + ": " + predictedNumericValue);

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
                    System.out.println("Predicted Nominal Value for Attribute " + attributeIndex + ": " + predictedClass);
                }
            }
            return ResponseEntity.ok(messageSource.getMessage("success.training.generated", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(messageSource.getMessage("error.training.failed", null, LocaleContextHolder.getLocale()));
        }
    }

    public ResponseEntity<?> createTraining(LocalDate startDay,
                                            LocalDate raceDay,
                                            Goal goal,
                                            Integer lengthOfRaceInMeters,
                                            Duration wantedTime,
                                            Integer actualRunLength,
                                            Duration actualTime,
                                            HttpServletRequest httpServletRequest) {

        if (goal.equals(RUN)) {
            ResponseEntity<?> test = testTrainingPossibility(startDay, raceDay, lengthOfRaceInMeters, wantedTime, actualRunLength, actualTime);
            System.out.println(test.getStatusCode() + " STATUS CODE");
            if (test.getStatusCode() == HttpStatus.OK) {
                String token = httpServletRequest.getHeader("Authorization");
                JWTUtils jwtUtils = new JWTUtils();
                User user = userRepository.findUserById(jwtUtils.getID(token).asLong());
                Training training = new Training(raceDay, goal, startDay, lengthOfRaceInMeters, user);
                trainingRepository.save(training);
                return MessageHandler.success(messageSource.getMessage("success.training.generated", null, LocaleContextHolder.getLocale()));
            } else {
                return test;
            }
        } else {
            return MessageHandler.error("coming soon");
        }

    }


    public ResponseEntity<?> testTrainingPossibility(LocalDate startDay,
                                                     LocalDate raceDay,
                                                     Integer lengthOfRaceInMeters,
                                                     Duration wantedTime,
                                                     Integer actualRunLength,
                                                     Duration actualTime) {
        System.out.println("A");
        if (!isGoalSuitable(lengthOfRaceInMeters)) {
            return MessageHandler.error("Run length is not suitable. Please choose between 1 km an 42 km");
        }
        double numberOfDays = ChronoUnit.DAYS.between(startDay, raceDay);
        System.out.println("number of days " + numberOfDays);
        double numberOfWeeks = numberOfDays / 7;
        System.out.println("number of weeks " + numberOfWeeks);
        double x = weeklyPercentageIncreaseInLength(numberOfWeeks, actualRunLength, lengthOfRaceInMeters);
        System.out.println(x);



/*
        if (!isGoalAchievable(startDay, raceDay, lengthOfRaceInMeters, wantedTimeInSeconds, actualRunLength, actualTimeInSeconds))
            return MessageHandler.error("Goal is too hard, training would not be safe");

        boolean temp = isGoalAchievable(startDay, raceDay, lengthOfRaceInMeters, wantedTimeInSeconds, actualRunLength, actualTimeInSeconds);
        System.out.println(temp + " BOOLEAN");
        if (temp == false) {
            return MessageHandler.error("Goal is too hard, training would not be safe");
        }
 */

        return MessageHandler.success(messageSource.getMessage("success.training.isAchievable", null, LocaleContextHolder.getLocale()));
    }

    public double weeklyPercentageIncreaseInLength(double numberOfWeeks, int currentRunLength, int raceLength) {
        int metersDifference = raceLength - currentRunLength;
        double weeklyDifference = metersDifference / numberOfWeeks;
        return weeklyDifference / currentRunLength * 100;

    }

    public Boolean isGoalSuitable(Integer lengthOfRaceInMeters) {
        return lengthOfRaceInMeters >= 1000 && lengthOfRaceInMeters <= 42195;
    }

    public Boolean hasActiveTraining(long id) {
        LocalDate today = LocalDate.now();
        List<Training> t = trainingRepository.findTrainingsContainingUser(id);
        for (Training training : t
        ) {
            if (today.isBefore(training.getRaceday()))
                return true;
        }
        return false;
    }


}
