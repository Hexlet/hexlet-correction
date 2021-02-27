package io.hexlet.typoreporter.domain.constraint;

import org.hibernate.validator.constraints.*;

import javax.validation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.*;
import java.lang.annotation.*;

@Null
@NotBlank
@ConstraintComposition(CompositionType.OR)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrNotBlank {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @OverridesAttribute(constraint = Null.class, name = "message")
    String nullMessage() default "{javax.validation.constraints.Null.message}";

    @OverridesAttribute(constraint = NotBlank.class, name = "message")
    String notBlankMessage() default "{javax.validation.constraints.NotBlank.message}";
}
