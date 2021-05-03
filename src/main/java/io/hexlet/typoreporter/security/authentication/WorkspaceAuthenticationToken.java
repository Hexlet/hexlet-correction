package io.hexlet.typoreporter.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class WorkspaceAuthenticationToken extends UsernamePasswordAuthenticationToken {

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>WorkspaceAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     *
     * @param principal   workspace name
     * @param credentials workspace api access token
     */
    public WorkspaceAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param principal   workspace name
     * @param credentials workspace api access token
     * @param authorities workspace authorities
     */
    public WorkspaceAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
