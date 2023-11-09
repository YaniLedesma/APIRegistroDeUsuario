package org.registration.services;

import org.registration.exceptions.EmailExistsException;
import org.registration.model.Phone;
import org.registration.model.User;
import org.registration.repository.UserRepository;
import org.registration.request.PhoneRequest;
import org.registration.request.RegisterRequest;
import org.registration.response.CommonResponse;
import org.registration.security.jwt.JwtService;
import org.registration.utils.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegisterService {
    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public CommonResponse register(RegisterRequest registerRequest) throws EmailExistsException {
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new EmailExistsException("El correo ya registrado");
        }

        UserValidator.validateUser(registerRequest);

        User user = createUser(registerRequest);
        userRepository.save(user);

        return createRegisterResponse(user);
    }


    private User createUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(user.getCreated());
        user.setIsActive(true);
        user.setToken(jwtService.getToken(user));

        savePhone(user, registerRequest.getPhones());
        return user;
    }

    static void savePhone(User user, List<PhoneRequest> phones) {
        List<Phone> newPhones = new ArrayList<>();

        if (phones != null) {
            for (PhoneRequest phoneReq : phones) {
                Phone phone = new Phone();
                phone.setNumber(phoneReq.getNumber());
                phone.setCityCode(phoneReq.getCitycode());
                phone.setCountryCode(phoneReq.getCountrycode());

                newPhones.add(phone);
            }
        }

        user.setPhones(newPhones);
    }

    private CommonResponse createRegisterResponse(User user) {
        return CommonResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .modified(user.getModified())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isActive(user.getIsActive())
                .build();
    }
}
