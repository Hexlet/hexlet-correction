package io.hexlet.typoreporter.service.dto.oauth2;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OAuth2UserInfo {
    private String email;
    private String nickname;
    private String firstName;
    private String lastName;
}
