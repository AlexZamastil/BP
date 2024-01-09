package cz.uhk.fim.project.bakalarka.model;

import jakarta.persistence.*;

@Entity
@Table(name = "picture")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_pictureid")
    private long id;

    @Column(name = "picturedata",columnDefinition = "bytea")
    private byte[] pictureData;



    public Picture(long id, byte[] pictureData) {
        this.id = id;
        this.pictureData = pictureData;
    }

    public Picture(byte[] pictureData) {
        this.pictureData = pictureData;
    }

    public Picture() {
    }

    public byte[] getPictureData() {
        return pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
    }
}
