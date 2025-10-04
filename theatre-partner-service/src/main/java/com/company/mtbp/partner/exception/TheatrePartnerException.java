package com.company.mtbp.partner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TheatrePartnerException extends RuntimeException {
    public TheatrePartnerException(String message) {
        super(message);
    }

    public TheatrePartnerException(String message, Throwable tw) {
        super(message, tw);
    }
}
