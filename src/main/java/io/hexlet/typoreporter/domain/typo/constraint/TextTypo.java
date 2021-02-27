package io.hexlet.typoreporter.domain.typo.constraint;

import javax.validation.*;
import javax.validation.constraints.*;
import java.lang.annotation.*;

@NotNull
@Size(max = 1000)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TextTypo {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
