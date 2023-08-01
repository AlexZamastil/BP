package java.cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "day")
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_dayid")
    private long id;

    @Column(name = "date")
    private LocalDate date;

    @Column (name = "caloriesgained")
    private double caloriesgained;

    @Column (name = "caloriesburned")
    private double caloriesburned;

    @ManyToOne
    @JoinColumn(name = "fk_trainingid")
    private Training training;


    @OneToMany
    @JoinTable(name = "dayexercise",
            joinColumns = {
                    @JoinColumn(name = "fk_dayid", referencedColumnName = "pk_dayid")},
            inverseJoinColumns = {
                    @JoinColumn(name = "fk_exerciseid", referencedColumnName = "pk_exerciseid")}
    )
    private Set<Exercise> exercises = new HashSet<>();

    @OneToMany
    @JoinTable(name = "dayfood",
            joinColumns = {
                    @JoinColumn(name = "fk_dayid", referencedColumnName = "pk_dayid")},
            inverseJoinColumns = {
                    @JoinColumn(name = "fk_foodid", referencedColumnName = "pk_foodid")}
    )
    private Set<Food> menu = new HashSet<>();

    public Day() {
    }

    public Day(long id, LocalDate date, double caloriesgained, double caloriesburned, Training training, Set<Exercise> exercises, Set<Food> menu) {
        this.id = id;
        this.date = date;
        this.caloriesgained = caloriesgained;
        this.caloriesburned = caloriesburned;
        this.training = training;
        this.exercises = exercises;
        this.menu = menu;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getCaloriesgained() {
        return caloriesgained;
    }

    public void setCaloriesgained(double caloriesgained) {
        this.caloriesgained = caloriesgained;
    }

    public double getCaloriesburned() {
        return caloriesburned;
    }

    public void setCaloriesburned(double caloriesburned) {
        this.caloriesburned = caloriesburned;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }
}
