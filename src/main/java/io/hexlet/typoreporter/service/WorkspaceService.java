package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import io.hexlet.typoreporter.web.exception.WorkspaceAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository repository;

    private final ConversionService conversionService;

    @Transactional(readOnly = true)
    public List<WorkspaceInfo> getAllWorkspacesInfo() {
        return repository.findAll()
            .stream()
            .map(wks -> conversionService.convert(wks, WorkspaceInfo.class))
            .toList();
    }

    @Transactional(readOnly = true)
    public Optional<WorkspaceInfo> getWorkspaceInfoByName(final String wksName) {
        return repository.getWorkspaceByName(wksName)
            .map(workspace -> conversionService.convert(workspace, WorkspaceInfo.class));
    }

    @Transactional(readOnly = true)
    public boolean existsWorkspaceByName(final String wksName) {
        return repository.existsWorkspaceByName(wksName);
    }

    @Transactional(readOnly = true)
    public boolean existsWorkspaceByUrl(final String wksUrl) {
        return repository.existsWorkspaceByName(wksUrl);
    }

    @Transactional
    public WorkspaceInfo createWorkspace(final CreateWorkspace createWks) {
        if (repository.existsWorkspaceByName(createWks.name())) {
            throw new WorkspaceAlreadyExistException("name", createWks.name());
        }
        if (repository.existsWorkspaceByUrl(createWks.url())) {
            throw new WorkspaceAlreadyExistException("url", createWks.url());
        }
        final var wksToCreate = requireNonNull(conversionService.convert(createWks, Workspace.class));
        wksToCreate.setApiAccessToken(UUID.randomUUID());
        final var createdWks = repository.save(wksToCreate);
        return conversionService.convert(createdWks, WorkspaceInfo.class);
    }

    @Transactional
    public Optional<WorkspaceInfo> updateWorkspace(final CreateWorkspace updateWks, final String oldWksName) {
        if (!oldWksName.equals(updateWks.name()) && repository.existsWorkspaceByName(updateWks.name())) {
            throw new WorkspaceAlreadyExistException("name", updateWks.name());
        }
        //TODO add update wks url, need check if it's not the same url and if it doesn't exist
        return repository.getWorkspaceByName(oldWksName)
            .map(oldWks -> oldWks.setName(updateWks.name()))
            .map(oldWks -> oldWks.setDescription(updateWks.description()))
            .map(repository::save)
            .map(wks -> conversionService.convert(wks, WorkspaceInfo.class));
    }

    @Transactional
    public Integer deleteWorkspaceByName(final String wksName) {
        return repository.deleteWorkspaceByName(wksName);
    }

    @Transactional(readOnly = true)
    public Optional<UUID> getWorkspaceApiAccessTokenByName(String wksName) {
        return repository.getWorkspaceByName(wksName)
            .map(Workspace::getApiAccessToken);
    }

    @Transactional
    public Optional<UUID> regenerateWorkspaceApiAccessTokenByName(String wksName) {
        final var newToken = UUID.randomUUID();
        final var numberRowAffected = repository.updateApiAccessTokenByWorkspaceName(wksName, newToken);
        return numberRowAffected == 0 ? Optional.empty() : Optional.of(newToken);
    }

    @Transactional(readOnly = true)
    public Optional<Workspace> getWorkspaceByName(final String name) {
        return repository.getWorkspaceByName(name);
    }
}
