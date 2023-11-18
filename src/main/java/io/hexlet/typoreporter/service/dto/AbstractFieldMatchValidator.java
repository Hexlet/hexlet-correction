package io.hexlet.typoreporter.service.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.PropertyAccessorFactory;

import java.lang.annotation.Annotation;

import static java.lang.String.format;

public abstract class AbstractFieldMatchValidator<T extends Annotation> implements ConstraintValidator<T, Object> {
    protected String firstFieldName;
    protected String secondFieldName;
    protected String message;

    protected abstract boolean areFieldsValid(Object firstField, Object secondField);

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext context) {
        final var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        final var firstObj = beanWrapper.getPropertyValue(firstFieldName);
        final var secondObj = beanWrapper.getPropertyValue(secondFieldName);
        final var isValid = areFieldsValid(firstObj, secondObj);

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
