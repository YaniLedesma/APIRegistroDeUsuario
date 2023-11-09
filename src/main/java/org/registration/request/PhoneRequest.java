package org.registration.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneRequest {
    private String number;
    private String citycode;
    private String countrycode;
}
