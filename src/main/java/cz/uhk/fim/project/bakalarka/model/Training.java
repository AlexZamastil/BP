package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.Goal;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_trainingid")
    private long id;

    @Column(name = "raceday")
    private LocalDate raceday;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Goal type;

    @Column(name = "startday")
    private LocalDate startday;

    @Column(name = "lengthinmeters")
    private Integer lengthinmeters;

    @OneToOne
    @JoinColumn(name = "fk_userid")
    private User user;

    public Training(){

    }

    public Training(long id, LocalDate finalday, Goal type, LocalDate startday, Integer lengthinmeters, User user) {
        this.id = id;
        this.raceday = finalday;
        this.type = type;
        this.startday = startday;
        this.lengthinmeters = lengthinmeters;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getFinalday() {
        return raceday;
    }

    public void setFinalday(LocalDate finalday) {
        this.raceday = finalday;
    }

    public Goal getType() {
        return type;
    }

    public void setType(Goal type) {
        this.type = type;
    }

    public LocalDate getStartday() {
        return startday;
    }

    public void setStartday(LocalDate startday) {
        this.startday = startday;
    }

    public double getLengthinmeters() {
        return lengthinmeters;
    }

    public void setLengthinmeters(Integer lengthinmeters) {
        this.lengthinmeters = lengthinmeters;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
