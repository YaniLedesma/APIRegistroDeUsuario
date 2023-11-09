package org.registration.exceptions;

import org.springframework.validation.annotation.Validated;

@Validated
public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String message) {
        super(message);
    }

}
