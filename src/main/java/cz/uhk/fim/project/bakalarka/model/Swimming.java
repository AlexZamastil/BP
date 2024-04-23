package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.SwimmingStyle;
import jakarta.persistence.*;
import lombok.Data;
/**
 * Entity class representing a Swimming, that implements ExerciseType and specifies data of Exercise entity.
 *
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
@Entity
@Table(name = "swimming")
@Data
public class Swimming implements ExerciseType{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_swimmingid")
    private long id;

    @Column(name = "lengthinmeters")
    private Integer lenglhinmeters;

    @Column(name = "swimmingstyle")
    @Enumerated(EnumType.STRING)
    private SwimmingStyle swimmingstyle;

    @OneToOne
    @JoinColumn(name = "fk_exerciseid")
    private Exercise exercise;

    public Swimming() {
    }
    @Override
    public String getType() {
        return "SWIMMING";
    }

    public Swimming(Integer lenglhinmeters, SwimmingStyle swimmingstyle, Exercise exercise) {
        this.lenglhinmeters = lenglhinmeters;
        this.swimmingstyle = swimmingstyle;
        this.exercise = exercise;
    }
}

