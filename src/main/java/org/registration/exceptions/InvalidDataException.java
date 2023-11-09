package org.registration.exceptions;

import org.springframework.validation.annotation.Validated;

@Validated
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}
