package com.project.taskmanagement.controller;

import com.project.taskmanagement.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;

@ControllerAdvice
public class MyControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException usernameNotFoundException) {
        return new ResponseEntity<>("The username is not found!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleExistingUsername(EntityExistsException entityExistsException) {
        return new ResponseEntity<>("The username is already taken!", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleWrongCredentials(BadCredentialsException badCredentialsException) {
        return new ResponseEntity<>("Wrong credentials!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoPermission.class)
    public ResponseEntity<?> handleWrongCredentials(NoPermission noPermission) {
        return new ResponseEntity<>("You don't have permission to do this!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WrongEmployeeId.class)
    public ResponseEntity<?> handleWrongEmployeeId(WrongEmployeeId wrongEmployeeId) {
        return new ResponseEntity<>("You've entered wrong employee id!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongCompanyName.class)
    public ResponseEntity<?> handleWrongCompanyName(WrongCompanyName wrongCompanyName) {
        return new ResponseEntity<>("You've entered wrong company name!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeFromAnotherCompany.class)
    public ResponseEntity<?> handleEmployeeFromAnotherCompany(EmployeeFromAnotherCompany employeeFromAnotherCompany) {
        return new ResponseEntity<>("This employee works for another company!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongTaskId.class)
    public ResponseEntity<?> handleWrongTaskId(WrongTaskId wrongTaskId) {
        return new ResponseEntity<>("You've entered wrong task id!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NonExistingTaskId.class)
    public ResponseEntity<?> handleNonExistingTaskId(NonExistingTaskId nonExistingTaskId) {
        return new ResponseEntity<>("The task with provided id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongUsername.class)
    public ResponseEntity<?> handleWrongUsername(WrongUsername wrongUsername) {
        return new ResponseEntity<>("You've entered wrong username!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordsDontMatch.class)
    public ResponseEntity<?> handlePasswordsMismatch(PasswordsDontMatch passwordsDontMatch) {
        return new ResponseEntity<>("The actual and entered passwords don't match!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongEmployeeLastName.class)
    public ResponseEntity<?> handleWrongEmployeeLastName(WrongEmployeeLastName wrongEmployeeLastName) {
        return new ResponseEntity<>("The actual and entered lastnames don't match!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongEmployeeName.class)
    public ResponseEntity<?> handleWrongEmployeeName(WrongEmployeeName wrongEmployeeName) {
        return new ResponseEntity<>("The actual and entered names don't match!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<?> handleWrongEmployeeName(AuthorizationException authorizationException) {
        return new ResponseEntity<>("You are not authorized", HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>("Please change http method type!", HttpStatus.METHOD_NOT_ALLOWED);
    }




}
