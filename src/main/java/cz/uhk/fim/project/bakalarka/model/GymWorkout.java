package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "gymworkout")
@Data
public class GymWorkout {
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
