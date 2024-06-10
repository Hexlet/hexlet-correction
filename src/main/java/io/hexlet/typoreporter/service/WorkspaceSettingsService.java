package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.workspace.AllowedUrl;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import io.hexlet.typoreporter.handler.exception.AllowedUrlAlreadyExistException;
import io.hexlet.typoreporter.handler.exception.AllowedUrlNotFoundException;
import io.hexlet.typoreporter.repository.AllowedUrlRepository;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.repository.WorkspaceSettingsRepository;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import io.hexlet.typoreporter.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceSettingsService {

    private final WorkspaceSettingsRepository workspaceSettingsRepository;
    private final WorkspaceRepository workspaceRepository;
    private final AllowedUrlRepository allowedUrlRepository;
    private final WorkspaceService service;

    @Transactional(readOnly = true)
    public UUID getWorkspaceApiAccessTokenById(Long wksId) {
        return workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceId(wksId)
            .map(WorkspaceSettings::getApiAccessToken)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksId));
    }

    @Transactional(readOnly = true)
    public WorkspaceSettings getWorkspaceSettingsByWorkspaceId(Long wksId) {
        return workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceId(wksId)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksId));
    }

    //top
    @Transactional
    public void regenerateWorkspaceApiAccessTokenById(Long wksId) {
        final var maybeWksSettings = workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceId(wksId);
        if (maybeWksSettings.isEmpty()) {
            throw new WorkspaceNotFoundException(wksId);
        }
        final var wksSettings = maybeWksSettings.get();
        wksSettings.setApiAccessToken(UUID.randomUUID());
        workspaceSettingsRepository.save(wksSettings);
    }

    @Transactional(readOnly = true)
    public Page<AllowedUrl> getPagedAllowedUrlsByWorkspaceId(Pageable pageable, Long wksId) {
        if (!service.existsWorkspaceById(wksId)) {
            throw new WorkspaceNotFoundException(wksId);
        }

        return allowedUrlRepository.findPageAllowedUrlByWorkspaceId(pageable, wksId);
    }

    @Transactional
    public void addAllowedUrlToWorkspace(Long wksId, String url) {

        String trimmedUrl = TextUtils.trimUrl(url);

        if (allowedUrlRepository.findAllowedUrlByUrlAndWorkspaceId(trimmedUrl, wksId).isPresent()) {
            throw new AllowedUrlAlreadyExistException(trimmedUrl, wksId);
        }

        final Workspace workspace = workspaceRepository.getWorkspaceById(wksId)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksId));

        AllowedUrl allowedUrl = new AllowedUrl();
        allowedUrl.setUrl(trimmedUrl);
        allowedUrl.setWorkspace(workspace);

        allowedUrlRepository.save(allowedUrl);
    }

    @Transactional
    public void removeAllowedUrlFromWorkspace(Long wksId, String url) {
        String trimmedUrl = TextUtils.trimUrl(url);

        final Workspace workspace = workspaceRepository.getWorkspaceById(wksId)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksId));

        final AllowedUrl allowedUrl = allowedUrlRepository.findAllowedUrlByUrlAndWorkspaceId(url, wksId)
            .orElseThrow(() -> new AllowedUrlNotFoundException(trimmedUrl, wksId));

        allowedUrlRepository.delete(allowedUrl);
    }
}
