package org.registration.services;

import org.registration.model.User;
import org.registration.repository.UserRepository;
import org.registration.request.LoginRequest;
import org.registration.response.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public LoginService(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        User user = auth.getPrincipal() instanceof User ? (User) auth.getPrincipal() : null;
        modifyLastLogin(user);

        return LoginResponse.builder()
                .message("Login successful")
                .build();
    }

    private void modifyLastLogin(User user) {
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }
}
