package io.hexlet.hexletcorrection.web.rest.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String objectName;

    private final String field;

    private final String message;
}
