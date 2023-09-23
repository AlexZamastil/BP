package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "exercise")

public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_exerciseid")
    private long id;

    @Column(name = "name")
    private String name;
    @Column(name = "name_eng")
    private String name_eng;

    @Column(name = "picture", columnDefinition = "bytea")
    private byte[] picture;

    @Column(name = "description")
    private String description;
    @Column(name = "description_eng")
    private String description_eng;


    public Exercise() {

    }

    public Exercise(long id, String name, byte[] picture, String description) {

        this.id = id;
        this.name = name;
        this.picture = picture;
        this.description = description;
    }

    public Exercise(String name, String name_eng, byte[] picture, String description, String description_eng) {
        this.name = name;
        this.name_eng = name_eng;
        this.picture = picture;
        this.description = description;
        this.description_eng = description_eng;
    }

    public Exercise(String name, String name_eng, String description, String description_eng) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
