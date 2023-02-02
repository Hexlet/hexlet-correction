package io.hexlet.typoreporter.domain.workspace.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull(message = "Workspace name must not be null")
@NotBlank(message = "Workspace name must not be blank")
@NotEmpty(message = "Workspace name must not be empty")
@Size(min = 2, max = 20)
@Pattern(regexp = "^[-_A-Za-z0-9]*$", message = "Workspace name must contain A-Z a-z 0-9 _ - characters")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkspaceName {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
