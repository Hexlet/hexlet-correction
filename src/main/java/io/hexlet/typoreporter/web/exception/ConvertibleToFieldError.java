package io.hexlet.typoreporter.web.exception;

import org.springframework.validation.FieldError;

public interface ConvertibleToFieldError {

    FieldError toFieldError(String objectName);
}
