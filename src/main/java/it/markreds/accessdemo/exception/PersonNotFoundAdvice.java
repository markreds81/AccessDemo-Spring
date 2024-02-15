package it.markreds.accessdemo.exception;

import it.markreds.accessdemo.exception.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class PersonNotFoundAdvice {
    public record PersonNotFoundMessage(Date timestamp, int status, String error) { }

    @ResponseBody
    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public PersonNotFoundMessage handlePersonNotFoundException (PersonNotFoundException ex) {
        return new PersonNotFoundMessage(new Date(), HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}
