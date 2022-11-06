package io.hexlet.typoreporter.security.model;

import io.hexlet.typoreporter.domain.role.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public interface SecuredAccount extends UserDetails {

    @Override
    default Collection<? extends GrantedAuthority> getAuthorities() {
       return getRoles();
    }

    @Value("#{target.roles}")
    Set<Role> getRoles();

    @Override
    @Value("#{target.password}")
    String getPassword();

    @Override
    @Value("#{target.username}")
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
