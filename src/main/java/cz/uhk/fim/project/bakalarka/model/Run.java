package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.RunCategory;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "run")
@Data
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

    public Run(Integer lenglhinmeters, RunCategory runcategory, Exercise exercise) {
        this.lenglhinmeters = lenglhinmeters;
        this.runcategory = runcategory;
        this.exercise = exercise;
    }

}
