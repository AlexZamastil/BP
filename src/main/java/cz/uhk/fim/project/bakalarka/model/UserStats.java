package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * Entity class representing a UserStats entity. This is stored additional user's data, that are not his personal information.
 *
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
@Entity
@Table(name = "userstats")
@Data
@AllArgsConstructor
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_userstatsid")
    private long id;

    @Column(name = "bmi")
    private double bmi;

    @Column(name = "waterintake")
    private double waterintake;
    @Column(name = "averagerunlength")
    private Integer averageRunLength;

    @Column(name = "averagerunpace")
    private Double averageRunPace;

    @Column(name="bmr")
    private Double bmr;

    @OneToOne
    @JoinColumn(name = "fk_userid")
    private User user;

    public UserStats() {
    }

    public UserStats(double bmi, double waterintake, double bmr, Integer averageRunLength, Double averageRunPace, User user) {
        this.bmi = bmi;
        this.waterintake = waterintake;
        this.bmr = bmr;
        this.averageRunLength = averageRunLength;
        this.averageRunPace = averageRunPace;
        this.user = user;
    }
}
