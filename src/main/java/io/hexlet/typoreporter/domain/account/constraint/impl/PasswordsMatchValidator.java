package io.hexlet.typoreporter.domain.account.constraint.impl;

import io.hexlet.typoreporter.domain.account.constraint.PasswordsMustMatch;
import io.hexlet.typoreporter.service.dto.account.UpdatePassword;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator <PasswordsMustMatch, UpdatePassword> {

    @Override
    public boolean isValid(final UpdatePassword updatePassword,
                           final ConstraintValidatorContext constraintValidatorContext) {

        final String newPassword = updatePassword.getNewPassword();
        return newPassword != null && newPassword.equals(updatePassword.getConfirmPassword());
    }
}
