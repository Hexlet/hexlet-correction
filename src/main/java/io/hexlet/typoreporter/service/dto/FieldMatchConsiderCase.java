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
 * {@code @FieldMatch.List({
 *
 * @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
 * @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
 * })}
 */
@Constraint(validatedBy = FieldMatchConsiderCaseValidator.class)
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

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @see FieldMatchConsiderCase
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        FieldMatchConsiderCase[] value();
    }
}
