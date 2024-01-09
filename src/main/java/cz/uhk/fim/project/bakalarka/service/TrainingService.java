package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.DAO.TrainingRepository;
import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.enumerations.Goal;
import cz.uhk.fim.project.bakalarka.model.Training;
import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.*;
import weka.core.converters.ConverterUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static cz.uhk.fim.project.bakalarka.enumerations.Goal.RUN;

@Service
public class TrainingService {

    public String dataset = "src/main/java/cz/uhk/fim/project/bakalarka/files/dataset_1.arff";
    private J48 trainedModel;
    private Classifier classifier;
    private Instances data;
    UserRepository userRepository;
    TrainingRepository trainingRepository;
    public final double MAX_PERCENTAGE_INCREASE = 2.5;

    public TrainingService(UserRepository userRepository, TrainingRepository trainingRepository) {
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
    }

    public void trainModel () throws Exception{
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

    public ResponseEntity<?> generateTrainingPlan(int weight,
                                                  int height,
                                                  double bmi,
                                                  String sex,
                                                  String bodyType,
                                                  int availibleTrainingDays,
                                                  int trainingLengthInDays,
                                                  int trainingLengthInMeters,
                                                  double trainingPace,
                                                  int raceLength,
                                                  double racePaceToAchieve,
                                                  String raceType,
                                                  String raceElevation
    ){

        try{
            int[] attributeIndices = {13,14};
            for (int attributeIndex : attributeIndices) {

                Instance instance = new DenseInstance(16);

                instance.setDataset(data);

                instance.setValue(0,weight);
                instance.setValue(1,height);
                instance.setValue(2,bmi);
                instance.setValue(3,sex);
                instance.setValue(4,bodyType);
                instance.setValue(5,availibleTrainingDays);
                instance.setValue(6,trainingLengthInDays);
                instance.setValue(7,trainingLengthInMeters);
                instance.setValue(8,trainingPace);
                instance.setValue(9,raceLength);
                instance.setValue(10,racePaceToAchieve);
                instance.setValue(11,raceType);
                instance.setValue(12,raceElevation);

                data.setClassIndex(attributeIndex);

                double[] predictions = classifier.distributionForInstance(instance);

                int type= data.attribute(attributeIndex).type();
                System.out.println("attribue type is " + type);

                if (type == Attribute.NUMERIC){
                    // get numeric data
                    double predictedNumericValue = classifier.classifyInstance(instance);
                    System.out.println("Predicted Numeric Value for Attribute " + attributeIndex + ": " + predictedNumericValue);

                }
                else if (type == Attribute.NOMINAL){
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

            return ResponseEntity.ok("training plan generated");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("training generation failed");
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
            ResponseEntity<?> test = testRunSpecs(startDay, raceDay, lengthOfRaceInMeters, wantedTime, actualRunLength, actualTime);
            System.out.println(test.getStatusCode() + " STATUS CODE");
            if (test.getStatusCode() == HttpStatus.OK) {
                String token = httpServletRequest.getHeader("Authorization");
                JWTUtils jwtUtils = new JWTUtils();
                User user = userRepository.findUserById(jwtUtils.getID(token).asLong());
                Training training = new Training(raceDay, goal, startDay, lengthOfRaceInMeters, user);
                trainingRepository.save(training);
                return MessageHandler.success("Training saved successfully");
            } else {
                return test;
            }
        } else {
            return MessageHandler.error("comming soon");
        }

    }


    public ResponseEntity<?> testRunSpecs(LocalDate startDay,
                                          LocalDate raceDay,
                                          Integer lengthOfRaceInMeters,
                                          Duration wantedTime,
                                          Integer actualRunLength,
                                         Duration actualTime) {
        if (!isGoalSuitable(lengthOfRaceInMeters)) {
            return MessageHandler.error("Run length is not suitable. Please choose between 1 km an 42 km");
        }
/*
        if (wantedTime) {
            wantedTime = (int) (lengthOfRaceInMeters / (6 / 3.6));//6km/h = slow jogging, divide to get m/s
        }

        if (!isGoalAchievable(startDay, raceDay, lengthOfRaceInMeters, wantedTimeInSeconds, actualRunLength, actualTimeInSeconds))
            return MessageHandler.error("Goal is too hard, training would not be safe");


        boolean temp = isGoalAchievable(startDay, raceDay, lengthOfRaceInMeters, wantedTimeInSeconds, actualRunLength, actualTimeInSeconds);
        System.out.println(temp + " BOOLEAN");
        if (temp == false) {
            return MessageHandler.error("Goal is too hard, training would not be safe");
        }

 */

        return MessageHandler.success("This goal is achievable");
    }

    public Boolean isGoalSuitable(Integer lengthOfRaceInMeters) {
        return lengthOfRaceInMeters >= 1000 && lengthOfRaceInMeters <= 42195;
    }
    /*

    public Boolean isGoalAchievable(LocalDate startDay,
                                    LocalDate raceDay,
                                    Integer lengthOfRaceInMeters,
                                    Integer wantedTimeInSeconds,
                                    Integer actualRunLength,
                                    Integer actualTimeInSeconds) {

        double numberOfDays = ChronoUnit.DAYS.between(startDay, raceDay);
        System.out.println("number of days " + numberOfDays);

        double numberOfWeeks = numberOfDays / 7;
        System.out.println("number of weeks " + numberOfWeeks);

        double weeksOfVolumeExtension = numberOfWeeks / 2; //This value might change based on further research
        System.out.println("weeks of volume extension " + weeksOfVolumeExtension);

        double weeksOfIntensityExtension = numberOfWeeks - weeksOfVolumeExtension;
        if (lengthOfRaceInMeters > 10000) {
            weeksOfIntensityExtension = numberOfWeeks - weeksOfVolumeExtension - 2; // 2 weeks of taper phase
        }
        System.out.println("weeks of intensity extrension " + weeksOfIntensityExtension);

        double lengthDifference = lengthOfRaceInMeters - actualRunLength;
        System.out.println("length difference " + lengthDifference);

        double weeklyVolumeIncrease = lengthDifference / weeksOfVolumeExtension;
        System.out.println("weekly volume increase" + weeklyVolumeIncrease);

        double percentageWeeklyVolumeIncrease = weeklyVolumeIncrease / actualRunLength * 100;
        System.out.println("percentage weekly volume increase " + percentageWeeklyVolumeIncrease);

        double timeDifference = wantedTimeInSeconds - actualTimeInSeconds;
        System.out.println("time difference " + timeDifference);
        double weeklyIntensityIncrease = timeDifference / weeksOfIntensityExtension;
        System.out.println("weekly intensity increase " + weeklyIntensityIncrease);
        double percentageWeeklyIntensityIncrease = weeklyIntensityIncrease / actualTimeInSeconds * 100;
        System.out.println("percentage weekly intensity increase " + percentageWeeklyIntensityIncrease);
        //TODO DODELAT, VYRESIT % A VZIT V UVAHU TEMPO
        return percentageWeeklyVolumeIncrease >= 0 &&
                !(percentageWeeklyVolumeIncrease > MAX_PERCENTAGE_INCREASE) &&
                percentageWeeklyIntensityIncrease >= 0 &&
                !(percentageWeeklyIntensityIncrease > MAX_PERCENTAGE_INCREASE);
    }

    */

    public Boolean hasActiveTraining(long id) {
        LocalDate today = LocalDate.now();
        List<Training> t = trainingRepository.findTrainingsContainingUser(id);
        for (Training training : t
        ) {
            if (today.isBefore(training.getFinalday()))
                return true;
        }
        return false;
    }



}
