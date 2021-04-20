package io.hexlet.typoreporter.domain.workspace.constraint;

import javax.validation.*;
import javax.validation.constraints.*;
import java.lang.annotation.*;

@NotBlank(message = "Workspace description must not be blank")
@Size(min = 2, max = 1000)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkspaceDescription {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
