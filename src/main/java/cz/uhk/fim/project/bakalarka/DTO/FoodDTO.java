package cz.uhk.fim.project.bakalarka.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FoodDTO {
    String name;
    String name_eng;
    String description;
    String description_eng;

    int calories;

    List<String> tags;
    String tagsJSON;
    public FoodDTO(String name, String name_eng, String description, String description_eng, int calories,String tags) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.calories = calories;
        this.tagsJSON = tags;
    }
}
