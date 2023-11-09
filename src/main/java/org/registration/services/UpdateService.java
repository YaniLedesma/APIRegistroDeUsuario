package org.registration.services;

import org.registration.model.User;
import org.registration.repository.PhoneRepository;
import org.registration.repository.UserRepository;
import org.registration.request.PhoneRequest;
import org.registration.request.UpdateRequest;
import org.registration.response.LoginResponse;
import org.registration.utils.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class UpdateService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PhoneRepository phoneRepository;

    public UpdateService(UserRepository userRepository, PasswordEncoder passwordEncoder, PhoneRepository phoneRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.phoneRepository = phoneRepository;
    }

    public LoginResponse update(String email, UpdateRequest updateRequest) {
        String message = "Update successful";
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user != null) {
            UserValidator.validatePassword(updateRequest.getPassword());
            updateUser(user, updateRequest);
        }

        return LoginResponse.builder()
                .message(message)
                .build();
    }

    @Transactional
    public void updateUser(User user, UpdateRequest updateRequest) {
        user.setName(updateRequest.getName());
        user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        user.setModified(LocalDateTime.now());
        user.setIsActive(updateRequest.getIsActive());
        deletePhoneExist(user);
        savePhone(user, updateRequest.getPhones());

        userRepository.save(user);
    }

    private void savePhone(User user, List<PhoneRequest> phones) {
    }

    public void deletePhoneExist(User user) {
        user.getPhones().forEach(phone -> phoneRepository.delete(phone));
    }

}
