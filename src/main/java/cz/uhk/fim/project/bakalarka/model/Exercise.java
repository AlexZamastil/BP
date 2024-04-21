package cz.uhk.fim.project.bakalarka.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing an Exercise. Picture of the exercise is stored separately.
 * Exercises are assigned to Day entities in Training.
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
@Entity
@Table(name = "exercise")
@Data
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_exerciseid")
    private long id;

    @Column(name = "name")
    private String name;
    @Column(name = "name_eng")
    private String name_eng;

    @Column(name = "description")
    private String description;
    @Column(name = "description_eng")
    private String description_eng;
    @Column(name = "caloriesburned")
    private int caloriesBurned;
    @OneToOne
    @JoinColumn(name = "fk_pictureid", referencedColumnName = "pk_pictureid")
    private Picture picture;

    @ManyToMany
    @JoinTable(
            name = "tagexercise",
            joinColumns = @JoinColumn(name = "fk_exerciseid"),
            inverseJoinColumns = @JoinColumn(name = "fk_tagid")
    )
    private Set<Tag> tagexercise = new HashSet<>();

    public Exercise() {

    }

    public Exercise(String name, String name_eng, Picture picture, String description, String description_eng, int caloriesBurned) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.caloriesBurned = caloriesBurned;
        this.picture = picture;
    }

    public Exercise(String name, String name_eng, String description, String description_eng, int caloriesBurned) {
        this.name = name;
        this.name_eng = name_eng;
        this.description = description;
        this.description_eng = description_eng;
        this.caloriesBurned = caloriesBurned;
    }

    public void addTagExercise(Tag t) {
        this.tagexercise.add(t);
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", name_eng='" + name_eng + '\'' +
                ", description='" + description + '\'' +
                ", description_eng='" + description_eng + '\'' +
                ", tagexercise=" + tagexercise +
                '}';
    }
}
