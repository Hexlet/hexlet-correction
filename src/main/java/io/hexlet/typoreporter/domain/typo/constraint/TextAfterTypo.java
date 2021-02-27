package io.hexlet.typoreporter.domain.typo.constraint;

import org.hibernate.validator.constraints.*;

import javax.validation.*;
import javax.validation.constraints.*;
import java.lang.annotation.*;

@Null
@Size(max = 100)
@ConstraintComposition(CompositionType.OR)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TextAfterTypo {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
