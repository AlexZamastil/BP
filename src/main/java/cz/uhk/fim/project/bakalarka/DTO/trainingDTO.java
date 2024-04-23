package cz.uhk.fim.project.bakalarka.DTO;

import cz.uhk.fim.project.bakalarka.enumerations.ElevationProfile;
import cz.uhk.fim.project.bakalarka.enumerations.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class trainingDTO {
    LocalDate startDay;
    LocalDate raceDay;
    Type type;
    Integer lengthOfRaceInMeters;
    Double wantedPace;
    ElevationProfile elevationProfile;
    boolean monday;
    boolean tuesday;
    boolean wednesday;
    boolean thursday;
    boolean friday;
    boolean saturday;
    boolean sunday;

    public int countTrainingDays() {
        int count = 0;
        if (monday) count++;
        if (tuesday) count++;
        if (wednesday) count++;
        if (thursday) count++;
        if (friday) count++;
        if (saturday) count++;
        if (sunday) count++;
        return count;
    }


}
