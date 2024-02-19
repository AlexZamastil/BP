package cz.uhk.fim.project.bakalarka.DTO;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
    private Long userId;

}
