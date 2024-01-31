package cz.uhk.fim.project.bakalarka.model;

import cz.uhk.fim.project.bakalarka.enumerations.BodyType;
import cz.uhk.fim.project.bakalarka.enumerations.Sex;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user", schema = "public")
public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "pk_userid")
    private long id;

    @Column(name = "customindex")
    private float customIndex;

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

    @Column(name = "adminprivileges")
    private boolean adminPrivileges;
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
    public User() {

    }


    public User(long id, float customIndex, LocalDate dateOfBirth, String email, double height, String nickname, String password,BodyType bodyType, double weight, boolean adminPrivileges, Set<Food> bannedFood, String token) {
        this.id = id;
        this.customIndex = customIndex;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.height = height;
        this.nickname = nickname;
        this.password = password;
        this.bodyType = bodyType;
        this.weight = weight;
        this.adminPrivileges = adminPrivileges;
        this.bannedFood = bannedFood;
        this.token = token;
    }
    public User(String email, String nickname, String password, LocalDate dateOfBirth, Sex sex){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getCustomIndex() {
        return customIndex;
    }

    public void setCustomIndex(float customIndex) {
        this.customIndex = customIndex;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodytype) {
        this.bodyType = bodytype;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isAdminPrivileges() {
        return adminPrivileges;
    }

    public void setAdminPrivileges(boolean adminPrivileges) {
        this.adminPrivileges = adminPrivileges;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Set<Food> getBannedFood() {
        return bannedFood;
    }

    public void setBannedFood(Set<Food> bannedFood) {
        this.bannedFood = bannedFood;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", customIndex=" + customIndex +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                ", height=" + height +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", weight=" + weight +
                ", adminPrivileges=" + adminPrivileges +
                ", token='" + token + '\'' +
                ", bannedFood=" + bannedFood +
                '}';
    }
}
