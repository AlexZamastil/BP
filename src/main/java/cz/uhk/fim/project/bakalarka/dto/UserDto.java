package cz.uhk.fim.project.bakalarka.dto;

import cz.uhk.fim.project.bakalarka.enumerations.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String email;
    private String nickname;
    private String password;
    private LocalDate birthdate;
    private Sex sex;

}
