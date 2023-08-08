package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_foodid")
    private long id;

    @Column(name = "calories")
    private double calories;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private byte[] picture;

    @OneToMany
    @JoinTable(name = "foodingredient",
            joinColumns = {
                    @JoinColumn(name = "fk_foodid", referencedColumnName = "pk_foodid")},
            inverseJoinColumns = {
                    @JoinColumn(name = "fk_ingredientid", referencedColumnName = "pk_ingredientid")}
    )
    private Set<Ingredient> ingredients = new HashSet<>();


    public Food() {

    }

    public Food(long id, double calories, String description, String name, byte[] picture, Set<Ingredient> ingredients) {
        this.id = id;
        this.calories = calories;
        this.description = description;
        this.name = name;
        this.picture = picture;
        this.ingredients = ingredients;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
