package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_ingredientid")
    private long id;

    @Column(name = "amountingrams")
    private Integer amountingrams;

    @Column(name = "calories")
    private double calories;

    @Column(name = "fats")
    private double fats;

    @Column(name = "name")
    private String name;

    @Column(name = "protein")
    private double protein;

    @Column(name = "sugar")
    private double sugar;

    public Ingredient() {

    }

    public Ingredient(long id, Integer amountingrams, double calories, double fats, String name, double protein, double sugar) {
        this.id = id;
        this.amountingrams = amountingrams;
        this.calories = calories;
        this.fats = fats;
        this.name = name;
        this.protein = protein;
        this.sugar = sugar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getAmountingrams() {
        return amountingrams;
    }

    public void setAmountingrams(Integer amountingrams) {
        this.amountingrams = amountingrams;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }
}
