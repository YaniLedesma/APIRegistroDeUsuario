package org.registration.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UpdateRequest {
    private String name;
    private String password;
    private Boolean isActive;
    private List<PhoneRequest> phones;
}
