package io.hexlet.typoreporter.handler.exception;

import org.springframework.validation.FieldError;

public interface ConvertibleToFieldError {

    FieldError toFieldError(String objectName);
}
