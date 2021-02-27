package io.hexlet.typoreporter.domain.typo.constraint;


import io.hexlet.typoreporter.domain.constraint.NullOrNotBlank;

import javax.validation.*;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Size(max = 200)
@NullOrNotBlank(nullMessage = "Reporter comment must be null", notBlankMessage = "Reporter comment must not be blank")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReporterComment {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
