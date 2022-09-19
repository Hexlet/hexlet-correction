package io.hexlet.typoreporter.security.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public interface SecuredAccount extends UserDetails {

    @Override
    default Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_USER");
    }

    @Override
    @Value("#{target.password}")
    String getPassword();

    @Override
    @Value("#{target.username}")
    String getUsername();

}
