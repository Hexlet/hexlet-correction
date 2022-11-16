package io.hexlet.typoreporter.service.dto;

import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;

    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext context) {
        final var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        final var firstObj = beanWrapper.getPropertyValue(firstFieldName);
        final var secondObj = beanWrapper.getPropertyValue(secondFieldName);
        final var isValid = Objects.equals(firstObj, secondObj);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode(firstFieldName)
                .addConstraintViolation();
        }
        return isValid;
    }
}
