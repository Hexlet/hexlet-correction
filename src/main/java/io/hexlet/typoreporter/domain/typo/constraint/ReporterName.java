package io.hexlet.typoreporter.domain.typo.constraint;

import javax.validation.*;
import javax.validation.constraints.*;
import java.lang.annotation.*;

@NotNull
@NotBlank
@Size(max = 50)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReporterName {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
