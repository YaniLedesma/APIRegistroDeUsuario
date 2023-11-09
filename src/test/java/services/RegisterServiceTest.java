package services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.registration.exceptions.EmailExistsException;
import org.registration.model.Phone;
import org.registration.model.User;
import org.registration.repository.UserRepository;
import org.registration.request.PhoneRequest;
import org.registration.request.RegisterRequest;
import org.registration.response.CommonResponse;
import org.registration.security.jwt.JwtService;
import org.registration.services.RegisterService;
import org.registration.utils.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private RegisterService registerService;

    public RegisterServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void userRegistration_successfulRegistration() throws EmailExistsException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Mary Poppins");
        registerRequest.setEmail("mary.poppins@example.com");
        registerRequest.setPassword("Password1234");
        registerRequest.setPhones(Collections.singletonList(new PhoneRequest("123456789", "1", "1")));

        User expectedUser = new User();
        UUID uuid = UUID.randomUUID();
        expectedUser.setId(uuid);
        expectedUser.setName("Mary Poppins");
        expectedUser.setEmail("mary.poppins@example.com");
        expectedUser.setPassword("encodedPassword");
        expectedUser.setCreated(LocalDateTime.now());
        expectedUser.setLastLogin(expectedUser.getCreated());
        expectedUser.setIsActive(true);
        expectedUser.setToken("jwtToken");

        Phone phone = new Phone();
        phone.setId(1L);
        phone.setNumber("123456789");
        phone.setCityCode("1");
        phone.setCountryCode("1");
        expectedUser.setPhones(Collections.singletonList(phone));

        Mockito.when(passwordEncoder.encode("Password1234")).thenReturn("encodedPassword");
        Mockito.when(jwtService.getToken(Mockito.any(User.class))).thenReturn("jwtToken");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);

        CommonResponse registerResponse = registerService.register(registerRequest);

        assertEquals(expectedUser.getCreated().toLocalDate(), registerResponse.getCreated().toLocalDate());
        assertEquals(expectedUser.getModified(), registerResponse.getModified());
        assertEquals(expectedUser.getLastLogin().toLocalDate(), registerResponse.getLastLogin().toLocalDate());
        assertEquals(expectedUser.getToken(), registerResponse.getToken());
        assertEquals(expectedUser.getIsActive(), registerResponse.getIsActive());
    }

    @Test
    void userRegistration_duplicateEmail_EmailExistsExceptionThrown() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("mary.poppins@example.com");

        Mockito.when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new org.registration.model.User()));

        assertThrows(EmailExistsException.class,
                () -> registerService.register(registerRequest));
    }
}
