package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.BodyType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "userstats")
@Data
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_userstatsid")
    private long id;

    @Column(name = "bmi")
    private double bmi;

    @Column(name = "waterneeded")
    private double waterneeded;

    @OneToOne
    @JoinColumn(name = "fk_userid")
    private User user;

    public UserStats() {
    }

    public UserStats(double bmi,  double waterneeded, User user) {
        this.bmi = bmi;
        this.waterneeded = waterneeded;
        this.user = user;
    }

}
