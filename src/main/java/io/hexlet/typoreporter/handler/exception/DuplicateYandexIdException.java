package io.hexlet.typoreporter.handler.exception;

public class DuplicateYandexIdException extends RuntimeException {
    public DuplicateYandexIdException(String message) {
        super(message);
    }
}
