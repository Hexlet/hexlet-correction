package io.hexlet.typoreporter.service.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.Objects;

import static java.text.MessageFormat.format;

public class FieldMatchIgnoreCaseValidator implements ConstraintValidator<FieldMatchIgnoreCase, Object> {

    protected String firstFieldName;

    protected String secondFieldName;

    protected String message;

    @Override
    public void initialize(final FieldMatchIgnoreCase constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext context) {
        final var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        final var firstObj = beanWrapper.getPropertyValue(firstFieldName);
        final var secondObj = beanWrapper.getPropertyValue(secondFieldName);
        final var isValid = Objects.equals(firstObj.toString().toLowerCase(), secondObj.toString().toLowerCase());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(format(message, firstObj, secondObj))
                .addPropertyNode(firstFieldName)
                .addConstraintViolation();
            context.buildConstraintViolationWithTemplate(format(message, firstObj, secondObj))
                .addPropertyNode(secondFieldName)
                .addConstraintViolation();
        }
        return isValid;
    }
}
