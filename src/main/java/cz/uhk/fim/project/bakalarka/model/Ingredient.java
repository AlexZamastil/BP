package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;
/**
 * Entity class representing an ingredient of Food entity. Food consists of multiple ingredients.
 *
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
@Entity
@Table(name = "ingredient")
@Data
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

}
