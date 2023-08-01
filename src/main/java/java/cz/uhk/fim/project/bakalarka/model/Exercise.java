package java.cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_exerciseid")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "description")
    private String description;


    public Exercise() {

    }

    public Exercise(long id, String name, byte[] picture, String description) {

        this.id = id;
        this.name = name;
        this.picture = picture;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
