package cz.uhk.fim.project.bakalarka.DTO;

import cz.uhk.fim.project.bakalarka.enumerations.Sex;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    private LocalDate dateOfBirth;
    private String email;
    private String nickname;
    private String password;
    private Sex sex;

}
