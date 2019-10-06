package spittr.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import spittr.exceptions.DuplicateException;

//handles all errors across the app
@ControllerAdvice
public class ExceptionHandlerExample {

    @ExceptionHandler(DuplicateException.class)
    public String duplicateSpittleHandler() {
        return "error/duplicate";
    }

}

