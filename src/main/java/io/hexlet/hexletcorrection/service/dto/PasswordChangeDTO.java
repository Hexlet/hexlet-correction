package io.hexlet.hexletcorrection.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representing a password change required data - current and new password.
 */
@Data
@NoArgsConstructor// Empty constructor needed for Jackson.
@AllArgsConstructor
public class PasswordChangeDTO {

    private String currentPassword;

    private String newPassword;
}
