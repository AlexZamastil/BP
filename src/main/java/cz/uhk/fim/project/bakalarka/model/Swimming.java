package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.SwimmingStyle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "swimming")
@Data
@NoArgsConstructor
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

    public Swimming(Integer lenglhinmeters, SwimmingStyle swimmingstyle, Exercise exercise) {
        this.lenglhinmeters = lenglhinmeters;
        this.swimmingstyle = swimmingstyle;
        this.exercise = exercise;
    }

}

