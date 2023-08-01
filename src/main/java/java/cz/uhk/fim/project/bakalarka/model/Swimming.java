package java.cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.SwimmingStyle;
import jakarta.persistence.*;

@Entity
@Table(name = "swimming")
public class Swimming {
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

    public Swimming(long id, Integer lenglhinmeters, SwimmingStyle swimmingstyle, Exercise exercise) {
        this.id = id;
        this.lenglhinmeters = lenglhinmeters;
        this.swimmingstyle = swimmingstyle;
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

    public SwimmingStyle getSwimmingstyle() {
        return swimmingstyle;
    }

    public void setSwimmingstyle(SwimmingStyle swimmingstyle) {
        this.swimmingstyle = swimmingstyle;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}

