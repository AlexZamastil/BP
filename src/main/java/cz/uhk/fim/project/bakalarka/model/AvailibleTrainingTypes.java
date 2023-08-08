package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;

@Entity
@Table(name = "availabletrainingtypes")
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

    public AvailibleTrainingTypes() {
    }

    public AvailibleTrainingTypes(long id, boolean gym, boolean swimmimgpool, Training training) {
        this.id = id;
        this.gym = gym;
        this.swimmimgpool = swimmimgpool;
        this.training = training;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public boolean isSwimmimgpool() {
        return swimmimgpool;
    }

    public void setSwimmimgpool(boolean swimmimgpool) {
        this.swimmimgpool = swimmimgpool;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }
}
