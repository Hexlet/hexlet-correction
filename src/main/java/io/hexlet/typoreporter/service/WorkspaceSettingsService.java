package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import io.hexlet.typoreporter.repository.WorkspaceSettingsRepository;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceSettingsService {

    private final WorkspaceSettingsRepository repository;

    @Transactional(readOnly = true)
    public UUID getWorkspaceApiAccessTokenById(Long wksId) {
        return repository.getWorkspaceSettingsByWorkspaceId(wksId)
            .map(WorkspaceSettings::getApiAccessToken)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksId));
    }

    @Transactional(readOnly = true)
    public WorkspaceSettings getWorkspaceSettingsByWorkspaceId(Long wksId) {
        return repository.getWorkspaceSettingsByWorkspaceId(wksId)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksId));
    }

    //top
    @Transactional
    public void regenerateWorkspaceApiAccessTokenById(Long wksId) {
        final var maybeWksSettings = repository.getWorkspaceSettingsByWorkspaceId(wksId);
        if (maybeWksSettings.isEmpty()) {
            throw new WorkspaceNotFoundException(wksId);
        }
        final var wksSettings = maybeWksSettings.get();
        wksSettings.setApiAccessToken(UUID.randomUUID());
        repository.save(wksSettings);
    }
}
