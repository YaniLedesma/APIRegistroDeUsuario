package org.registration.controller;

import org.registration.exceptions.EmailExistsException;
import org.registration.request.LoginRequest;
import org.registration.request.RegisterRequest;
import org.registration.request.UpdateRequest;
import org.registration.response.LoginResponse;
import org.registration.services.LoginService;
import org.registration.services.RegisterService;
import org.registration.services.UpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final RegisterService registerService;
    private final LoginService loginService;
    private final UpdateService updateService;

    public UserController(RegisterService registerService, LoginService loginService, UpdateService updateService) {
        this.registerService = registerService;
        this.loginService = loginService;
        this.updateService = updateService;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.ok(registerService.register(registerRequest));
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws EmailExistsException {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }

    @PutMapping(value = "/update/{email}", consumes = "application/json")
    public ResponseEntity<LoginResponse> update(@PathVariable String email, @RequestBody @Valid UpdateRequest updateRequest) {
        return ResponseEntity.ok(updateService.update(email, updateRequest));
    }

}
