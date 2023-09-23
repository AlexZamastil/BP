package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.RunCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "run")
public class Run {
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

    public Run(long id, Integer lenglhinmeters, RunCategory runcategory, Exercise exercise) {
        this.id = id;
        this.lenglhinmeters = lenglhinmeters;
        this.runcategory = runcategory;
        this.exercise = exercise;
    }

    public Run(Integer lenglhinmeters, RunCategory runcategory, Exercise exercise) {
        this.lenglhinmeters = lenglhinmeters;
        this.runcategory = runcategory;
        this.exercise = exercise;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLenglhinmeters() {
        return lenglhinmeters;
    }

    public void setLenglhinmeters(Integer lenglhinmeters) {
        this.lenglhinmeters = lenglhinmeters;
    }

    public RunCategory getRuncategory() {
        return runcategory;
    }

    public void setRuncategory(RunCategory runcategory) {
        this.runcategory = runcategory;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
