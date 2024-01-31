package cz.uhk.fim.project.bakalarka.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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


    public List<String> getTags() {
        return tags;
    }



    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_eng() {
        return name_eng;
    }

    public void setName_eng(String name_eng) {
        this.name_eng = name_eng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription_eng() {
        return description_eng;
    }

    public void setDescription_eng(String description_eng) {
        this.description_eng = description_eng;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public MultipartFile getPictureData() {
        return pictureData;
    }

    public void setPictureData(MultipartFile pictureData) {
        this.pictureData = pictureData;
    }


    @Override
    public String toString() {
        return "ExerciseRequest{" +
                "name='" + name + '\'' +
                ", name_eng='" + name_eng + '\'' +
                ", description='" + description + '\'' +
                ", description_eng='" + description_eng + '\'' +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", style='" + style + '\'' +
                ", length=" + length +
                ", repetitions=" + repetitions +
                ", series=" + series +
                ", pictureData=" + pictureData +
                ", tagsJSON='" + tagsJSON + '\'' +
                ", tags=" + tags +
                '}';
    }
}
