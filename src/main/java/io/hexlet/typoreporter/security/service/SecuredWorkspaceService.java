package io.hexlet.typoreporter.security.service;

import io.hexlet.typoreporter.repository.WorkspaceSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecuredWorkspaceService implements UserDetailsService {

    private final WorkspaceSettingsRepository settingsRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return settingsRepository.getWorkspaceSettingsByWorkspaceName(name)
            .map(settings -> new User(name, settings.getApiAccessToken().toString(), List.of(() -> "WORKSPACE_API")))
            .orElseThrow(() -> new UsernameNotFoundException("Workspace with name='" + name + "' not found"));
    }
}
