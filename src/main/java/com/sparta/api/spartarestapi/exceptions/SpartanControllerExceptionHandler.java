package com.sparta.api.spartarestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.webjars.NotFoundException;

import javax.naming.NotContextException;
import javax.xml.bind.ValidationException;

@ControllerAdvice
public class SpartanControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidApiKeyException.class)
    ErrorMessage exceptionInvalidHandler(ValidationException e) {
        return new ErrorMessage("401", e.getMessage());
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    ErrorMessage exceptionBadHandler(ValidationException e) {
        return new ErrorMessage("400", e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({SpartanNotFoundException.class, CourseNotFoundException.class})
    ErrorMessage exceptionNotFoundHandler(RuntimeException e) {
        return new ErrorMessage("404", e.getMessage());
    }

}
