package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import io.hexlet.typoreporter.repository.WorkspaceSettingsRepository;
import io.hexlet.typoreporter.web.exception.WorkspaceNotFoundException;
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
    public UUID getWorkspaceApiAccessTokenByName(String wksName) {
        return repository.getWorkspaceSettingsByWorkspaceName(wksName)
            .map(WorkspaceSettings::getApiAccessToken)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksName));
    }

    @Transactional
    public void regenerateWorkspaceApiAccessTokenByName(String wksName) {
        final var maybeWksSettings = repository.getWorkspaceSettingsByWorkspaceName(wksName);
        if (maybeWksSettings.isEmpty()) {
            throw new WorkspaceNotFoundException(wksName);
        }
        final var wksSettings = maybeWksSettings.get();
        wksSettings.setApiAccessToken(UUID.randomUUID());
        repository.save(wksSettings);
    }
}
