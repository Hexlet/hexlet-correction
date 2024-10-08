package io.hexlet.typoreporter.service.dto.oauth2;

import lombok.Data;

@Data
public class PrivateEmail {
    private String email;
    private boolean verified;
    private boolean primary;
    private String visibility;
}
