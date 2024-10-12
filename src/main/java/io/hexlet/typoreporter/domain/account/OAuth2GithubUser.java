package io.hexlet.typoreporter.domain.account;

import io.hexlet.typoreporter.domain.workspace.AccountRole;
import io.hexlet.typoreporter.service.dto.oauth2.PrivateEmail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class OAuth2GithubUser extends DefaultOAuth2User {
    private final OAuth2User oAuth2User;
    private final PrivateEmail privateEmail;

    public OAuth2GithubUser(OAuth2User oAuth2User, PrivateEmail email) {
        super(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), "id");
        this.oAuth2User = oAuth2User;
        this.privateEmail = email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(AccountRole.ROLE_USER.name()));
        return authorities;
    }

    @Override
    public String getName() {
        return this.privateEmail.getEmail();
    }

    public String getEmail() {
        return this.privateEmail.getEmail();
    }

    public String getLogin() {
        return oAuth2User.getAttribute("login");
    }

    //TODO: fix required sets first and last names after issue #286 will be done (empty names)
    public String getFirstName() {
        String[] fullName = oAuth2User.<String>getAttribute("name").split(" ");
        return fullName[1] != null ? fullName[1] : "";
    }

    public String getLastName() {
        String[] fullName = oAuth2User.<String>getAttribute("name").split(" ");
        return fullName[0] != null ? fullName[0] : "";
    }

    public String getPassword() {
        Integer password = oAuth2User.getAttribute("id");
        return Objects.requireNonNull(password).toString();
    }

    public Integer getId() {
        return Objects.requireNonNull(oAuth2User.getAttribute("id"));
    }
}
