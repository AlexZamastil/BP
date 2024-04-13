package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entity class representing a Picture of an exercise. Pictures and other data are stored and accessed separately.
 *
 * This class maps database objects to its corresponding PostgreSQL database table.
 *
 * @author Alex Zamastil
 */
@Entity
@Table(name = "picture")
@Data
@NoArgsConstructor
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_pictureid")
    private long id;

    @Column(name = "picturedata",columnDefinition = "bytea")
    private byte[] pictureData;

    public Picture(byte[] pictureData) {
        this.pictureData = pictureData;
    }

}
