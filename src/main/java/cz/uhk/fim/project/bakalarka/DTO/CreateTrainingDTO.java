package cz.uhk.fim.project.bakalarka.DTO;

import cz.uhk.fim.project.bakalarka.enumerations.ElevationProfile;
import cz.uhk.fim.project.bakalarka.enumerations.Type;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class CreateTrainingDTO {
    LocalDate startDay;
    LocalDate raceDay;
    Type type;
    Integer lengthOfRaceInMeters;
    double wantedPace;
    ElevationProfile elevationProfile;
    boolean monday;
    boolean tuesday;
    boolean wednesday;
    boolean thursday;
    boolean friday;
    boolean saturday;
    boolean sunday;

}
