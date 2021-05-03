package io.hexlet.typoreporter.security.service;

import io.hexlet.typoreporter.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecuredWorkspaceService implements UserDetailsService {

    private final WorkspaceRepository repository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return repository.getSecuredWorkspaceByName(name)
            .orElseThrow(() -> new UsernameNotFoundException("Workspace with name='" + name + "' not found"));
    }
}
