package cz.uhk.fim.project.bakalarka.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    int length;
    int repetitions;
    int series;
    String tagsJSON;
    List<String> tags;

    //run
/*
    public ExerciseRequest(String name, String name_eng, String description, String description_eng, String category, int length, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.category = category;
        this.length = length;
        this.tagsJSON = tagsJSON;
    }
    public ExerciseRequest(String name, String name_eng, String description, String description_eng, String type, String category, int length, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.category = category;
        this.length = length;
        this.tagsJSON = tagsJSON;
    }

 */
    public ExerciseDTO(String name, String name_eng, String description, String description_eng, String type, String category, int length, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.category = category;
        this.length = length;
        this.tagsJSON = tagsJSON;
    }

    public ExerciseDTO(String name, String name_eng, String description, String description_eng, String category, int length, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.category = category;
        this.length = length;
        this.tagsJSON = tagsJSON;
    }
    //gym
/*
    public ExerciseRequest(String name, String name_eng, String description, String description_eng, int repetitions, int series, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.repetitions = repetitions;
        this.series = series;
        this.tagsJSON = tagsJSON;
    }

    public ExerciseRequest(String name, String name_eng, String description, String description_eng, String type, int repetitions, int series, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.repetitions = repetitions;
        this.series = series;
        this.tagsJSON = tagsJSON;
    }

 */

    public ExerciseDTO(String name, String name_eng, String description, String description_eng, int repetitions, int series, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.repetitions = repetitions;
        this.series = series;
        this.tagsJSON = tagsJSON;
    }

    public ExerciseDTO(String name, String name_eng, String description, String description_eng, String type, int repetitions, int series, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.repetitions = repetitions;
        this.series = series;
        this.tagsJSON = tagsJSON;
    }

    //swimming
/*
    public ExerciseRequest(String name, String name_eng, String description, String description_eng,int length, String style,  String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.style = style;
        this.length = length;
        this.tagsJSON = tagsJSON;
    }
    public ExerciseRequest(String name, String name_eng, String description, String description_eng, String type,int length, String style,  String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.style = style;
        this.length = length;
        this.tagsJSON = tagsJSON;
    }

 */
    public ExerciseDTO(String name, String name_eng, String description, String description_eng, String type, int length, String style, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.style = style;
        this.length = length;
        this.tagsJSON = tagsJSON;
    }

    public ExerciseDTO(String name, String name_eng, String description, String description_eng, int length, String style, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.style = style;
        this.length = length;
        this.tagsJSON = tagsJSON;
    }

}
