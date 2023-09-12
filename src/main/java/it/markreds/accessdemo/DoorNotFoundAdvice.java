package it.markreds.accessdemo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
class DoorNotFoundAdvice {

    record DoorNotFoundMessage(Date timestamp, int status, String error) { }

    @ResponseBody
    @ExceptionHandler(DoorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    DoorNotFoundMessage handleDoorNotFoundException (DoorNotFoundException ex) {
        return new DoorNotFoundMessage(new Date(), HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}
