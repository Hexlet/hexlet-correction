package io.hexlet.typoreporter.service.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountLogin {

    @NotBlank(message = "Account_login_dto email field must not be empty")
    private String email;

    @NotBlank(message = "Account_login_dto password field must not be empty")
    private String password;

}
