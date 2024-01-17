package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.workspace.AccountRole;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRoleId;
import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.repository.WorkspaceRoleRepository;
import io.hexlet.typoreporter.repository.WorkspaceSettingsRepository;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import io.hexlet.typoreporter.service.mapper.WorkspaceMapper;
import io.hexlet.typoreporter.handler.exception.AccountNotFoundException;
import io.hexlet.typoreporter.handler.exception.WorkspaceAlreadyExistException;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    private final WorkspaceSettingsRepository settingsRepository;

    private final WorkspaceMapper workspaceMapper;

    private final AccountRepository accountRepository;
    private final WorkspaceRoleRepository workspaceRoleRepository;

    @Transactional(readOnly = true)
    public List<WorkspaceInfo> getAllWorkspacesInfo() {
        return workspaceRepository.findAll()
            .stream()
            .map(workspaceMapper::toWorkspaceInfo)
            .toList();
    }

    @Transactional(readOnly = true)
    public Optional<WorkspaceInfo> getWorkspaceInfoById(final Long wksId) {
        return workspaceRepository.getWorkspaceById(wksId)
            .map(workspaceMapper::toWorkspaceInfo);
    }

    @Transactional(readOnly = true)
    public boolean existsWorkspaceById(final Long wksId) {
        return workspaceRepository.existsWorkspaceById(wksId);
    }

    @Transactional
    public WorkspaceInfo createWorkspace(final CreateWorkspace createWks, final String email) {
        if (workspaceRepository.existsWorkspaceByUrl(createWks.url())) {
            throw new WorkspaceAlreadyExistException("url", createWks.url());
        }

        final var wksToCreate = requireNonNull(workspaceMapper.toWorkspace(createWks));
        final var wksSettings = new WorkspaceSettings();
        wksSettings.setApiAccessToken(UUID.randomUUID());
        wksSettings.setWorkspace(wksToCreate);

        Account account = accountRepository.findAccountByEmail(email).orElseThrow();

        final var workspaceRoleId = new WorkspaceRoleId(wksToCreate.getId(), account.getId());
        final var workspaceRole = new WorkspaceRole(workspaceRoleId, AccountRole.ROLE_ADMIN, wksToCreate, account);
        wksToCreate.addWorkspaceRole(workspaceRole);

        settingsRepository.save(wksSettings);
        return workspaceMapper.toWorkspaceInfo(wksToCreate);
    }

    @Transactional
    public WorkspaceInfo updateWorkspace(final CreateWorkspace updateWks, final Long wksId) {
        final Workspace workspace = workspaceRepository.getWorkspaceById(wksId)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksId));
        final String updatedUrlValue = updateWks.url();
        if (!workspace.getUrl().equals(updatedUrlValue) && workspaceRepository.existsWorkspaceByUrl(updatedUrlValue)) {
            throw new WorkspaceAlreadyExistException("url", updatedUrlValue);
        }
        workspace.setName(updateWks.name());
        workspace.setDescription(updateWks.description());
        workspace.setUrl(updateWks.url());
        return workspaceMapper.toWorkspaceInfo(workspace);
    }

    @Transactional
    public Integer deleteWorkspaceById(final Long wksId) {
        return workspaceRepository.deleteWorkspaceById(wksId);
    }

    @Transactional(readOnly = true)
    public Optional<Workspace> getWorkspaceById(final Long wksId) {
        return workspaceRepository.getWorkspaceById(wksId);
    }

    @Transactional(readOnly = true)
    public List<WorkspaceInfo> getAllWorkspacesInfoByEmail(String email) {
        final var accountOptional = accountRepository.findAccountByEmail(email);
        return accountOptional.map(account -> account.getWorkspaceRoles().stream()
            .map(WorkspaceRole::getWorkspace)
            .map(workspaceMapper::toWorkspaceInfo)
            .toList()).orElseGet(ArrayList::new);
    }

    @Transactional
    public boolean isUserRelatedToWorkspace(Long wksId, String email) {
        final var accountOptional = accountRepository.findAccountByEmail(email);
        return accountOptional.map(account -> account.getWorkspaceRoles().stream()
                .map(WorkspaceRole::getWorkspace)
                .anyMatch(wks -> wks.getId().equals(wksId)))
            .orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean isAdminRoleUserInWorkspace(Long wksId, String email) {
        final var account = accountRepository.findAccountByEmail(email).
            orElseThrow(() -> new AccountNotFoundException(email));
        final var workspace = workspaceRepository.getWorkspaceById(wksId).
            orElseThrow(() -> new WorkspaceNotFoundException(wksId));
        final var workSpaceRoleOptional = workspaceRoleRepository.getWorkspaceRoleByAccountIdAndWorkspaceId(
            account.getId(),
            workspace.getId()
        );
        return workSpaceRoleOptional.filter(workspaceRole -> workspaceRole.getRole() == AccountRole.ROLE_ADMIN).isPresent();
    }
}
