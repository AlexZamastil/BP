package cz.uhk.fim.project.bakalarka.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
public class ExerciseRequest {
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
    MultipartFile pictureData;
    String tagsJSON;
    List<String> tags;

    public ExerciseRequest() {
    }
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
    public ExerciseRequest(String name, String name_eng, String description, String description_eng, String type, String category, int length, MultipartFile pictureData, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.category = category;
        this.length = length;
        this.pictureData = pictureData;
        this.tagsJSON = tagsJSON;
    }

    public ExerciseRequest(String name, String name_eng, String description, String description_eng, String category, int length, MultipartFile pictureData, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.category = category;
        this.length = length;
        this.pictureData = pictureData;
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

    public ExerciseRequest(String name, String name_eng, String description, String description_eng, int repetitions, int series, String tagsJSON, MultipartFile multipartFile) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.repetitions = repetitions;
        this.series = series;
        this.tagsJSON = tagsJSON;
        this.pictureData = multipartFile;
    }

    public ExerciseRequest(String name, String name_eng, String description, String description_eng, String type, int repetitions, int series, String tagsJSON, MultipartFile multipartFile) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.repetitions = repetitions;
        this.series = series;
        this.tagsJSON = tagsJSON;
        this.pictureData = multipartFile;
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
    public ExerciseRequest(String name, String name_eng, String description, String description_eng, String type, int length, String style,  MultipartFile pictureData, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.type = type;
        this.style = style;
        this.length = length;
        this.pictureData = pictureData;
        this.tagsJSON = tagsJSON;
    }

    public ExerciseRequest(String name, String name_eng, String description, String description_eng,  int length, String style, MultipartFile pictureData, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.style = style;
        this.length = length;
        this.pictureData = pictureData;
        this.tagsJSON = tagsJSON;
    }

}
