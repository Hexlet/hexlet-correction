package io.hexlet.typoreporter.security.service;

import io.hexlet.typoreporter.repository.WorkspaceSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecuredWorkspaceService implements UserDetailsService {

    private final WorkspaceSettingsRepository settingsRepository;

    @Override
    public UserDetails loadUserByUsername(String idStr) throws UsernameNotFoundException {
        try {
            return settingsRepository.findById(Long.parseLong(idStr))
                .map(settings -> User.withUsername(idStr)
                    .password(settings.getApiAccessToken().toString())
                    .authorities("ROLE_WORKSPACE_API")
                    .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("Workspace with id='" + idStr + "' not found"));
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Workspace id '" + idStr + "' is not a parsable long.", e);
        }
    }
}
