package io.hexlet.typoreporter.handler;

import io.hexlet.typoreporter.handler.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.login.AccountExpiredException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AccountAlreadyExistException.class)
    public ResponseEntity<String> handleAccountAlreadyExistException(AccountAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<String> handleAccountExpiredException(AccountExpiredException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(ex.getMessage());
    }

    @ExceptionHandler(NewPasswordTheSameException.class)
    public ResponseEntity<String> handleNewPasswordSameException(NewPasswordTheSameException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(OldPasswordWrongException.class)
    public ResponseEntity<String> handleOldPasswordWrongException(OldPasswordWrongException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TypeNotPresentException.class)
    public ResponseEntity<String> handleTypoNotFoundException(TypeNotPresentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(WorkspaceAlreadyExistException.class)
    public ResponseEntity<String> handleWorkSpaceAlreadyExistException(WorkspaceAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
