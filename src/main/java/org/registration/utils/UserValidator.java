package org.registration.utils;

import org.registration.exceptions.InvalidDataException;
import org.registration.request.RegisterRequest;

public class UserValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z].*[a-z])(?=.*[0-9].*[0-9]).{8,}$";

    public static void validateUser(RegisterRequest registerRequest) {
        validatePassword(registerRequest.getPassword());
        validateEmail(registerRequest.getEmail());
    }

    public static void validatePassword(String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new InvalidDataException("La contraseña debe tener al menos 1 mayúscula, 2 minúscula Y 2 números");
        }
    }

    private static void validateEmail(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new InvalidDataException("Formato de correo inválido");
        }
    }
}
