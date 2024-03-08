package cz.uhk.fim.project.bakalarka.model;


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

    @Column(name = "waterintake")
    private double waterintake;

    @OneToOne
    @JoinColumn(name = "fk_userid")
    private User user;

    public UserStats() {
    }

    public UserStats(double bmi,  double waterintake, User user) {
        this.bmi = bmi;
        this.waterintake = waterintake;
        this.user = user;
    }

}
