package org.registration.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private List<PhoneRequest> phones;
}
