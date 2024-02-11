package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "trainingdays")
@Data
public class TrainingDays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_trainingdaysid")
    private long id;

    @Column(name = "monday")
    private boolean monday;

    @Column(name = "tuesday")
    private boolean tuesday;

    @Column(name = "wednesday")
    private boolean wednesday;
    @Column(name = "thursday")
    private boolean thursday;
    @Column(name = "friday")
    private boolean friday;
    @Column(name = "saturday")
    private boolean saturday;
    @Column(name = "sunday")
    private boolean sunday;

    @OneToOne
    @JoinColumn(name = "fk_trainingid")
    private Training training;

}
