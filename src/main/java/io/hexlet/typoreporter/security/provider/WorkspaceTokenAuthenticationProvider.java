package io.hexlet.typoreporter.security.provider;

import io.hexlet.typoreporter.security.authentication.WorkspaceAuthenticationToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceTokenAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService service;

    private final PasswordEncoder passwordEncoder;

    public WorkspaceTokenAuthenticationProvider(
        @Qualifier("securedWorkspaceService") UserDetailsService service,
        @Qualifier("noOpPasswordEncoder") PasswordEncoder passwordEncoder
    ) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final var workspaceName = auth.getName();
        final var workspace = service.loadUserByUsername(workspaceName);
        final var token = auth.getCredentials().toString();
        if (passwordEncoder.matches(token, workspace.getPassword())) {
            return new WorkspaceAuthenticationToken(workspaceName, token, workspace.getAuthorities());
        }
        throw new BadCredentialsException("BadCredentialsException");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WorkspaceAuthenticationToken.class.equals(authentication);
    }
}
