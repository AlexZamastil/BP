package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.RunCategory;
import jakarta.persistence.*;
import lombok.Data;
/**
 * Entity class representing a Run, that implements ExerciseType and specifies data of Exercise entity.
 *
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
@Entity
@Table(name = "run")
@Data
public class Run implements ExerciseType{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_runid")
    private long id;

    @Column(name = "lengthinmeters")
    private Integer lenglhinmeters;

    @Column(name = "runcategory")
    @Enumerated(EnumType.STRING)
    private RunCategory runcategory;

    @OneToOne
    @JoinColumn(name = "fk_exerciseid")
    private Exercise exercise;

    public Run() {

    }
    @Override
    public String getType() {
        return "RUN";
    }

    public Run(Integer lenglhinmeters, RunCategory runcategory, Exercise exercise) {
        this.lenglhinmeters = lenglhinmeters;
        this.runcategory = runcategory;
        this.exercise = exercise;
    }

}
