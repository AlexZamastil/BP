package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.BodyType;
import cz.uhk.fim.project.bakalarka.enumerations.Role;
import cz.uhk.fim.project.bakalarka.enumerations.Sex;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a User. This entity holds all important user's personal data
 * besides BMI and daily water intake, that are stored in UserStats entity.
 *
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
@Entity
@Table(name = "user", schema = "public")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_userid")
    private long id;
    @Column(name = "dateofbirth")
    private LocalDate dateOfBirth;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "height")
    private double height;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "weight")
    private double weight;
    @Column(name = "bodytype")
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;
    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "token")
    private String token;



    @ManyToMany
    @JoinTable(name = "userbannedfood",
            joinColumns = {
                    @JoinColumn(name = "fk_userid", referencedColumnName = "pk_userid")},
            inverseJoinColumns = {
                    @JoinColumn(name = "fk_foodid", referencedColumnName = "pk_foodid")}
    )
    private Set<Food> bannedFood = new HashSet<>();

    public User(String email, String nickname, String password, LocalDate dateOfBirth, Sex sex){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.role = Role.ROLE_USER;
    }
    public User(String email, String nickname, String password, LocalDate dateOfBirth, Sex sex, double weight, double height, BodyType bodyType){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.role = Role.ROLE_USER;
        this.weight = weight;
        this.height = height;
        this.bodyType = bodyType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                ", height=" + height +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", weight=" + weight +
                ", bodyType=" + bodyType +
                ", sex=" + sex +
                ", role=" + role +
                ", token='" + token + '\'' +
                '}';
    }

}
