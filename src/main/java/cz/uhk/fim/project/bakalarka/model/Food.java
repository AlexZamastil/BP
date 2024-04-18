package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
/**
 * Entity class representing a Food entity.
 * Foods are assigned to Day entities in Training based on their calories and tags.
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
@Entity
@Table(name = "food")
@Data
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_foodid")
    private long id;

    @Column(name = "caloriesgained")
    private Integer caloriesGained;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "description_eng")
    private String description_eng;

    @Column(name = "name_eng")
    private String name_eng;

    @OneToOne
    @JoinColumn(name = "fk_pictureid", referencedColumnName = "pk_pictureid")
    private Picture picture;


    @ManyToMany
    @JoinTable(
            name = "tagfood",
            joinColumns = @JoinColumn(name = "fk_foodid"),
            inverseJoinColumns = @JoinColumn(name = "fk_tagid")
    )
    private Set<Tag> tagfood = new HashSet<>();

    @OneToMany
    @JoinTable(name = "foodingredient",
            joinColumns = {
                    @JoinColumn(name = "fk_foodid", referencedColumnName = "pk_foodid")},
            inverseJoinColumns = {
                    @JoinColumn(name = "fk_ingredientid", referencedColumnName = "pk_ingredientid")}
    )
    private Set<Ingredient> ingredients = new HashSet<>();

    public void addTagFood(Tag t) {
        this.tagfood.add(t);
    }

    public Food(int caloriesGained, String description, String name, String description_eng, String name_eng, Picture picture) {
        this.caloriesGained = caloriesGained;
        this.description = description;
        this.name = name;
        this.description_eng = description_eng;
        this.name_eng = name_eng;
        this.picture = picture;

    }
}
