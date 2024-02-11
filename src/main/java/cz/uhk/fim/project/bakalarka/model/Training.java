package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.Goal;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table
@Data
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

    public Training(LocalDate raceday, Goal type, LocalDate startday, Integer lengthinmeters, User user) {
        this.raceday = raceday;
        this.type = type;
        this.startday = startday;
        this.lengthinmeters = lengthinmeters;
        this.user = user;
    }

}
