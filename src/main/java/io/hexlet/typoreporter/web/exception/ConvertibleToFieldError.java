package io.hexlet.typoreporter.web.exception;

import org.springframework.validation.FieldError;

@FunctionalInterface
public interface ConvertibleToFieldError {

    FieldError toFieldError(String objectName);
}
