package services;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.registration.exceptions.InvalidDataException;
import org.registration.repository.UserRepository;
import org.registration.request.RegisterRequest;
import org.registration.utils.UserValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    public static final String VALID_PASSWORD = "ValidPassword123";

    private static final String INVALID_PASSWORD = "invalidpassword";

    private static final String VALID_EMAIL = "soyunmailvalido@dominio.com";

    public UserValidatorTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validatePassword_ValidPassword_NoExceptionThrown() {
        UserValidator userValidator = new UserValidator();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword(VALID_PASSWORD);

        userValidator.validatePassword(registerRequest.getPassword());
    }

    @Test
    void validatePassword_InvalidPassword_InvalidDataExceptionThrown() {
        UserValidator userValidator = new UserValidator();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword(INVALID_PASSWORD);

        assertThrows(InvalidDataException.class,
                () -> userValidator.validatePassword(registerRequest.getPassword()));
    }

    @Test
    void validateUser_validEmail_validPassword_NoExceptionThrown() {
        UserValidator userValidator = new UserValidator();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword(VALID_PASSWORD);
        registerRequest.setEmail(VALID_EMAIL);

        Mockito.when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());

        userValidator.validateUser(registerRequest);
    }

    @Test
    void validateUser_invalidEmail_validPassword_NoExceptionThrown() {
        UserValidator userValidator = new UserValidator();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword(VALID_PASSWORD);
        registerRequest.setEmail("invalidoemail.com");

        assertThrows(InvalidDataException.class,
                () -> userValidator.validateUser(registerRequest));
    }
}
