package io.hexlet.typoreporter.security.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public interface SecuredWorkspace extends UserDetails {

    @Override
    default Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "WORKSPACE_API");
    }

    @Override
    @Value("#{target.apiAccessToken}")
    String getPassword();

    @Override
    @Value("#{target.name}")
    String getUsername();

    @Override
    default boolean isAccountNonExpired() {
        return true;
    }

    @Override
    default boolean isAccountNonLocked() {
        return true;
    }

    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    default boolean isEnabled() {
        return true;
    }
}
