package io.hexlet.typoreporter.service.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.Objects;

import static java.text.MessageFormat.format;

public class FieldMatchIgnoreCaseValidator extends AbstractFieldMatchValidator<FieldMatchIgnoreCase> {
    @Override
    public void initialize(final FieldMatchIgnoreCase constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    protected boolean areFieldsValid(Object firstField, Object secondField) {
        return Objects.equals(firstField.toString().toLowerCase(), secondField.toString().toLowerCase());
    }
}
