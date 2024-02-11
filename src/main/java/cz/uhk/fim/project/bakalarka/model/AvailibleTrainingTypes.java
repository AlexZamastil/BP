package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "availabletrainingtypes")
@Data
@NoArgsConstructor
public class AvailibleTrainingTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_availabletrainingtypesid")
    private long id;

    @Column(name = "gym")
    private boolean gym;

    @Column(name = "swimmingpool")
    private boolean swimmimgpool;

    @OneToOne
    @JoinColumn(name = "fk_trainingid")
    private Training training;

}
