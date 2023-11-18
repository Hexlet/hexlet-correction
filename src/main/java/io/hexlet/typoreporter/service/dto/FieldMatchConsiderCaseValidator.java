package io.hexlet.typoreporter.service.dto;

import java.util.Objects;

public class FieldMatchConsiderCaseValidator extends AbstractFieldMatchValidator<FieldMatchConsiderCase> {
    @Override
    public void initialize(final FieldMatchConsiderCase constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    protected boolean areFieldsValid(Object firstField, Object secondField) {
        return Objects.equals(firstField, secondField);
    }
}
