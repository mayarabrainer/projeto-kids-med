package kids.med.web_application.infra.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class TratadorDeExceptions {

    @ExceptionHandler(NoSuchElementException.class)
    public String tratarErro404() {
        return "erro/404";
    }

    @ExceptionHandler(Exception.class)
    public String tratarErro500(Exception e) {
        return "erro/500";
    }

}