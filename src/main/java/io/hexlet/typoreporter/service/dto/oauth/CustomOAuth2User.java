package io.hexlet.typoreporter.service.dto.oauth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private final String email;
    private final String nickname;
    private final String firstName;
    private final String lastName;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String keyAttribute) {
        super(authorities, attributes, keyAttribute);
        this.email = (String) attributes.get("default_email");
        this.nickname = (String) attributes.get("login");
        this.firstName = (String) attributes.get("first_name");
        this.lastName = (String) attributes.get("last_name");
    }
}
