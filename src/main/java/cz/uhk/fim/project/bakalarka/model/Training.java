package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.Type;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
/**
 * Entity class representing a Training. The Training entity itself stored just basic data about the training.
 * The key part is the list of Day entities, that are referring to a training, that also store information about exercises and food.
 *
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
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
    private Type type;

    @Column(name = "startday")
    private LocalDate startday;

    @Column(name = "lengthinmeters")
    private Integer lengthinmeters;

    @OneToOne
    @JoinColumn(name = "fk_userid")
    private User user;

    public Training(){

    }

    public Training(LocalDate raceday, Type type, LocalDate startday, Integer lengthinmeters, User user) {
        this.raceday = raceday;
        this.type = type;
        this.startday = startday;
        this.lengthinmeters = lengthinmeters;
        this.user = user;
    }


}
