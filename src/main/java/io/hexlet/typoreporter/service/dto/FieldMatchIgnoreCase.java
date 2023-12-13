package io.hexlet.typoreporter.service.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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
 * {@code @FieldMatchConsiderCase(first = "password", second = "confirmPassword", message = "The password and it confirmation must match")}
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatchIgnoreCaseValidator.class)
@Documented
public @interface FieldMatchIgnoreCase {

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

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @see FieldMatchIgnoreCase
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        FieldMatchIgnoreCase[] value();
    }
}
