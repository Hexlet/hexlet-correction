package io.hexlet.typoreporter.service.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountLogin {

    private static final int MAX_PASSWORD = 30;

    @NotBlank
    @Email
    private String email;

    @Size(min = 8, max = MAX_PASSWORD, message = "Password size must be between 8 and 30 characters")
    private String password;

}
