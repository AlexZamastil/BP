package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;
/**
 * Entity class representing a GymWorkout, that implements ExerciseType and specifies data of Exercise entity.
 *
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
@Entity
@Table(name = "gymworkout")
@Data

public class GymWorkout implements ExerciseType{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_gymworkoutid")
    private long id;

    @Column(name = "series")
    private Integer series;

    @Column(name = "repetitions")
    private Integer repetitions;

    @OneToOne
    @JoinColumn(name = "fk_exerciseid")
    private Exercise exercise;

    public GymWorkout() {

    }

    public GymWorkout(Integer series, Integer repetitions, Exercise exercise) {
        this.series = series;
        this.repetitions = repetitions;
        this.exercise = exercise;
    }

}
