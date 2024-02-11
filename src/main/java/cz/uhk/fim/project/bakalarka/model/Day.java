package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "day")
@Data
@NoArgsConstructor
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

}
