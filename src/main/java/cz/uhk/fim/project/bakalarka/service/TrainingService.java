package cz.uhk.fim.project.bakalarka.service;

import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class TrainingService {
    public ResponseEntity<?> generateTraining(LocalDate startday,
                                              LocalDate raceDay,
                                              Integer lengthOfRaceInMeters,
                                              Integer wantedTimeInSeconds,
                                              Integer actualRunLength,
                                              Integer actualTimeInSeconds) {
               if(!isGoalSuitable(lengthOfRaceInMeters)) {
                   return MessageHandler.error("Run length is not suitable. Please choose between 1 km an 42 km");
               }
               return MessageHandler.success("temp");

    }


    public Boolean isGoalSuitable(Integer lengthOfRaceInMeters) {
        if (lengthOfRaceInMeters < 1000 || lengthOfRaceInMeters > 42195) {
            return false;
        } else return true;

    }

    public Boolean isGoalAchievable(LocalDate startday,
                                    LocalDate raceDay,
                                    Integer lengthOfRaceInMeters,
                                    Integer wantedTimeInSeconds,
                                    Integer actualRunLength,
                                    Integer actualTimeInSeconds) {


        if (wantedTimeInSeconds == 0) {
            wantedTimeInSeconds = (int) (lengthOfRaceInMeters / (6 / 3.6));//6km/h = slow jogging, divide to get m/s
        }
        long numberOfDays = ChronoUnit.DAYS.between(startday,raceDay);
        long numberOfWeeks = numberOfDays/7;
        return true;//temp
    }
}
