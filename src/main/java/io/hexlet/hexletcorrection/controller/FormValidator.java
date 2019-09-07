package io.hexlet.hexletcorrection.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public interface FormValidator {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    default Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BiFunction<String, String, String> mergeFunction = (str1, str2) -> str1 + "; " + str2;

        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(error -> nonNull(error.getDefaultMessage()))
                .collect(Collectors.toMap(FieldError::getField, ObjectError::getDefaultMessage, mergeFunction::apply));
    }
}
