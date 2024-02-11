package cz.uhk.fim.project.bakalarka.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private Long userId;

}
