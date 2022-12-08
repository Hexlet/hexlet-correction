package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.repository.WorkspaceSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceSettingsService {

    private final WorkspaceSettingsRepository repository;

    @Transactional(readOnly = true)
    public Optional<UUID> getWorkspaceApiAccessTokenByName(String wksName) {
        return repository.getApiAccessTokenByWorkspaceName(wksName);
    }

    @Transactional
    public Optional<UUID> regenerateWorkspaceApiAccessTokenByName(String wksName) {
        final var newToken = UUID.randomUUID();
        final var numberRowAffected = repository.updateApiAccessTokenByWorkspaceName(wksName, newToken);
        return numberRowAffected == 0 ? Optional.empty() : Optional.of(newToken);
    }
}
