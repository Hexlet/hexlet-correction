package io.hexlet.typoreporter.service.dto.oauth2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PrivateEmail {
    private String email;
    private boolean verified;
    private boolean primary;
    private String visibility;
}
