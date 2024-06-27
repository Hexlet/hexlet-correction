package io.hexlet.typoreporter.domain.account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oAuth2User;

    public CustomOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("email");
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }
    public String getLogin() {
        return oAuth2User.getAttribute("login");
    }
    public String getFirstName() {
        String[] fullName = Objects.requireNonNull(oAuth2User.<String>getAttribute("name")).split(" ");
        return fullName[1];
    }
    public String getLastName() {
        String[] fullName = Objects.requireNonNull(oAuth2User.<String>getAttribute("name")).split(" ");
        return fullName[0];
    }
    public String getPassword() {
        Integer password = oAuth2User.getAttribute("id");
        return Objects.requireNonNull(password).toString();
    }
}
