package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a day of a training plan. The training consists of a list of Days.
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
@Entity
@Table(name = "day")
@Data
@NoArgsConstructor
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_dayid")
    private long id;
    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    @Column (name = "caloriesgained")
    private double caloriesGained;

    @Column (name = "caloriesburned")
    private double caloriesBurned;

    @ManyToOne
    @JoinColumn(name = "fk_trainingid")
    private Training training;

    @ManyToOne
    @JoinColumn(name = "fk_userid")
    private User user;


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

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }
    public void addFood(Food food) {
        menu.add(food);
    }
    public int getDayCalories(){
        int calories = 0;
    for (Exercise e : exercises){
        calories -= e.getCaloriesBurned();
    }
    for (Food f : menu){
        calories+= f.getCaloriesGained();
    }
    return calories;
    }
}
