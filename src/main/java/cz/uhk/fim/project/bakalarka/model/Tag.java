
package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
@Data
@NoArgsConstructor
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
    public Tag(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", text='" + text ;
    }
}

