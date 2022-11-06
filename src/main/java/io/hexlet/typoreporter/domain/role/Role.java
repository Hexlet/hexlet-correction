package io.hexlet.typoreporter.domain.role;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    WATCHER,
    EDITOR,
    CREATOR;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
