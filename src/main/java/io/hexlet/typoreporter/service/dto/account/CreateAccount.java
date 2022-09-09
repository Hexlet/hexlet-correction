package io.hexlet.typoreporter.service.dto.account;

import javax.validation.constraints.*;

import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;

public record CreateAccount (
    @AccountUsername String username,
    @Email String email,
    String password, // TODO make annotation
    @NotBlank @Size(min = 1, max = 50) String firstName,
    @NotBlank @Size(min = 1, max = 50) String lastName
) {

}
