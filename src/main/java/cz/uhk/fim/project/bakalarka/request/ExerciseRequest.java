package cz.uhk.fim.project.bakalarka.request;

import java.util.Arrays;
import java.util.List;

public class ExerciseRequest {
    String name;
    String name_eng;
    String description;
    String description_eng;
    String type;
    String category_style;
    int length;
    int repetitions;
    int series;
    byte[] pictureData;
    String tagsJSON;
    List<String> tags;

    public ExerciseRequest() {
    }

    public ExerciseRequest(String name, String name_eng, String description, String description_eng, Integer repetitions, Integer series, byte[] pictureData, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.repetitions = repetitions;
        this.series = series;
        this.pictureData = pictureData;
        this.tagsJSON = tagsJSON;
    }

    public ExerciseRequest(String name, String name_eng, String description, String description_eng, Integer repetitions, Integer series, String  tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.repetitions = repetitions;
        this.series = series;
        this.tagsJSON = tagsJSON;
    }

    public ExerciseRequest(String name, String name_eng, String description, String description_eng, String category_style, Integer length,  byte[] pictureData, String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.category_style = category_style;
        this.length = length;
        this.pictureData = pictureData;
        this.tagsJSON = tagsJSON;
    }

    public ExerciseRequest(String name, String name_eng, String description, String description_eng, String category_style, Integer length,  String tagsJSON) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.category_style = category_style;
        this.length = length;
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

    public String getCategory_style() {
        return category_style;
    }

    public void setCategory_style(String category_style) {
        this.category_style = category_style;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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

    public byte[] getPictureData() {
        return pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
    }


    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + "\"," +
                "\"name_eng\":\"" + name_eng + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"description_eng\":\"" + description_eng + "\"," +
                "\"type\":\"" + type + "\"," +
                "\"category_style\":\"" + category_style + "\"," +
                "\"length\":" + length + "," +
                "\"repetitions\":" + repetitions + "," +
                "\"series\":" + series + "," +
                "\"pictureData\":" + Arrays.toString(pictureData) + "," +
                "\"tags\":" + tags +
                "}";
    }
}
