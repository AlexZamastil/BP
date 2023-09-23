package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.DataAccessObject.TrainingRepository;
import cz.uhk.fim.project.bakalarka.DataAccessObject.UserRepository;
import cz.uhk.fim.project.bakalarka.enumerations.Goal;
import cz.uhk.fim.project.bakalarka.model.Training;
import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.util.JWTUtils;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static cz.uhk.fim.project.bakalarka.enumerations.Goal.RUN;

@Service
public class TrainingService {


    UserRepository userRepository;
    TrainingRepository trainingRepository;
    public final double MAX_PERCENTAGE_INCREASE = 2.5;

    public TrainingService(UserRepository userRepository, TrainingRepository trainingRepository) {
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
    }

    public ResponseEntity<?> createTraining(LocalDate startDay,
                                            LocalDate raceDay,
                                            Goal goal,
                                            Integer lengthOfRaceInMeters,
                                            Integer wantedTimeInSeconds,
                                            Integer actualRunLength,
                                            Integer actualTimeInSeconds,
                                            HttpServletRequest httpServletRequest) {

        if (goal.equals(RUN)) {
            ResponseEntity<?> test = testRunSpecs(startDay, raceDay, lengthOfRaceInMeters, wantedTimeInSeconds, actualRunLength, actualTimeInSeconds);
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
                                          Integer wantedTimeInSeconds,
                                          Integer actualRunLength,
                                          Integer actualTimeInSeconds){
        if (!isGoalSuitable(lengthOfRaceInMeters)) {
            return MessageHandler.error("Run length is not suitable. Please choose between 1 km an 42 km");
        }

        if (wantedTimeInSeconds == 0) {
            wantedTimeInSeconds = (int) (lengthOfRaceInMeters / (6 / 3.6));//6km/h = slow jogging, divide to get m/s
        }
/*
        if (!isGoalAchievable(startDay, raceDay, lengthOfRaceInMeters, wantedTimeInSeconds, actualRunLength, actualTimeInSeconds))
            return MessageHandler.error("Goal is too hard, training would not be safe");

 */
        boolean temp = isGoalAchievable(startDay, raceDay, lengthOfRaceInMeters, wantedTimeInSeconds, actualRunLength, actualTimeInSeconds);
        System.out.println(temp + " BOOLEAN");
        if (temp == false){
            return MessageHandler.error("Goal is too hard, training would not be safe");
        }

        return MessageHandler.success("This goal is achievable");
    }

    public Boolean isGoalSuitable(Integer lengthOfRaceInMeters) {
        return lengthOfRaceInMeters >= 1000 && lengthOfRaceInMeters <= 42195;
    }

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
                !(percentageWeeklyVolumeIncrease > MAX_PERCENTAGE_INCREASE)&&
                percentageWeeklyIntensityIncrease >= 0 &&
                !(percentageWeeklyIntensityIncrease > MAX_PERCENTAGE_INCREASE);
    }
}
