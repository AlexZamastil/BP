package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "food")
@Data
@NoArgsConstructor
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

    @OneToOne
    @JoinColumn(name = "fk_pictureid", referencedColumnName = "pk_pictureid")
    private Picture picture;



    @OneToMany
    @JoinTable(name = "foodingredient",
            joinColumns = {
                    @JoinColumn(name = "fk_foodid", referencedColumnName = "pk_foodid")},
            inverseJoinColumns = {
                    @JoinColumn(name = "fk_ingredientid", referencedColumnName = "pk_ingredientid")}
    )
    private Set<Ingredient> ingredients = new HashSet<>();

}
