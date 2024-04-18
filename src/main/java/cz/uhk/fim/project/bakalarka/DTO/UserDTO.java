package cz.uhk.fim.project.bakalarka.DTO;

import cz.uhk.fim.project.bakalarka.enumerations.BodyType;
import cz.uhk.fim.project.bakalarka.enumerations.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private LocalDate dateOfBirth;
    private String email;
    private String nickname;
    private String password;
    private Sex sex;
    private int weight;
    private int height;
    private BodyType bodyType;
    private int averageRunLength;
    private double averageRunPace;

    public UserDTO(LocalDate dateOfBirth, String email, String nickname, String password, Sex sex) {
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.sex = sex;
    }

    public UserDTO(LocalDate dateOfBirth, String email, String nickname, String password, Sex sex, int weight, int height, BodyType bodyType) {
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.bodyType = bodyType;
    }
}
