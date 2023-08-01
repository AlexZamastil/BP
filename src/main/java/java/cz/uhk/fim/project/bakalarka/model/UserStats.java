package java.cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.BodyType;
import jakarta.persistence.*;

@Entity
@Table(name = "userstats")
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_userstatsid")
    private long id;

    @Column(name = "bmi")
    private double bmi;

    @Column(name = "bodytype")
    @Enumerated(EnumType.STRING)
    private BodyType bodytype;

    @Column(name = "waterneeded")
    private double waterneeded;

    @OneToOne
    @JoinColumn(name = "fk_userid")
    private User user;

    public UserStats() {
    }

    public UserStats(long id, double bmi, BodyType bodytype, double waterneeded, User user) {
        this.id = id;
        this.bmi = bmi;
        this.bodytype = bodytype;
        this.waterneeded = waterneeded;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public BodyType getBodytype() {
        return bodytype;
    }

    public void setBodytype(BodyType bodytype) {
        this.bodytype = bodytype;
    }

    public double getWaterneeded() {
        return waterneeded;
    }

    public void setWaterneeded(double waterneeded) {
        this.waterneeded = waterneeded;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
