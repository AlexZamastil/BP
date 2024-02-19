package cz.uhk.fim.project.bakalarka.DTO;

import cz.uhk.fim.project.bakalarka.enumerations.ElevationProfile;
import cz.uhk.fim.project.bakalarka.enumerations.Goal;
import lombok.Data;

import java.time.LocalDate;
@Data
public class CreateTrainingDTO {
    LocalDate startDay;
    LocalDate raceDay;
    Goal goal;
    Integer lengthOfRaceInMeters;
    String wantedTime;
    Integer actualRunLength;
    String actualTime;
    ElevationProfile elevationProfile;

    public CreateTrainingDTO(LocalDate startDay, LocalDate raceDay, Goal goal, Integer lengthOfRaceInMeters, String wantedTime, Integer actualRunLength, String actualTime) {
        this.startDay = startDay;
        this.raceDay = raceDay;
        this.goal = goal;
        this.lengthOfRaceInMeters = lengthOfRaceInMeters;
        this.wantedTime = wantedTime;
        this.actualRunLength = actualRunLength;
        this.actualTime = actualTime;

    }
}
