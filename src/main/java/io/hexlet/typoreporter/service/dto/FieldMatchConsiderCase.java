package io.hexlet.typoreporter.service.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation annotation to validate that 2 fields have the same value.
 * An array of fields and their matching confirmation fields can be supplied.
 * <br>
 * Example, compare 1 pair of fields:
 * <br>
 * {@code @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")}
 * <br>
 * Example, compare more than 1 pair of fields:
 * <br>
 * {@code @FieldMatchConsiderCase(
 * first = "password",
 * second = "confirmPassword",
 * message = "The password and it confirmation must match")}
 */
@Constraint(validatedBy = FieldMatchConsiderCaseValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMatchConsiderCase {

    String message() default "The {first} and {second} fields must be equal";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */
    String first();

    /**
     * @return The second field
     */
    String second();

}
