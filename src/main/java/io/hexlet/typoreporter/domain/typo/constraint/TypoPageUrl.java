package io.hexlet.typoreporter.domain.typo.constraint;

import org.hibernate.validator.constraints.URL;

import javax.validation.*;
import javax.validation.constraints.*;
import java.lang.annotation.*;

@URL
@NotNull
@NotBlank
@Size(max = 1000)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TypoPageUrl {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
