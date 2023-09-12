
package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_tagid")
    private long id;

    @Column(name = "text",unique = true)
    private String text;

    @ManyToMany
    @JoinTable(
            name = "tagfood",
            joinColumns = @JoinColumn(name = "fk_tagid"),
            inverseJoinColumns = @JoinColumn(name = "fk_foodid")
    )
    private Set<Food> tagfood = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "tagexercise",
            joinColumns = @JoinColumn(name = "fk_tagid"),
            inverseJoinColumns = @JoinColumn(name = "fk_exerciseid")
    )
    private Set<Exercise> tagexercise = new HashSet<>();





    public Tag(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Tag() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

