package cz.uhk.fim.project.bakalarka.DTO;

import cz.uhk.fim.project.bakalarka.model.Exercise;
import cz.uhk.fim.project.bakalarka.model.Food;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Set;
@Data
@AllArgsConstructor
public class DayDTO {
    private Date date;
    private double caloriesgained;
    private double caloriesburned;
    private Set<Exercise> exercises;
    private Set<Food> menu;
}
