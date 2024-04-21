package cz.uhk.fim.project.bakalarka.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class ExerciseDTO {
    String name;
    String name_eng;
    String description;
    String description_eng;
    String type;
    String category;
    String style;
    int calories;
    int length;
    int repetitions;
    int series;
    String tagsJSON;
    List<String> tags;

    public ExerciseDTO(String name, String name_eng, String description, String description_eng, String type, String categoryStyle, int length, int calories, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.category = categoryStyle;
        this.style = categoryStyle;
        this.length = length;
        this.calories = calories;
        this.tagsJSON = tagsJSON;
    }



    public ExerciseDTO(String name, String name_eng, String description, String description_eng, String type, int repetitions, int series,int calories, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.repetitions = repetitions;
        this.series = series;
        this.calories = calories;
        this.tagsJSON = tagsJSON;
    }


}
