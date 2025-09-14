package de.inderkothe.payment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED, reason = "Once is enough.")
public class AlreadyProcessedRequestException extends RuntimeException {
    public AlreadyProcessedRequestException() {
        super();
    }
}
