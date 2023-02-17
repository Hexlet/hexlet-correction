package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.workspace.AccountRole;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRoleId;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.repository.WorkspaceRoleRepository;
import io.hexlet.typoreporter.web.exception.AccountNotFoundException;
import io.hexlet.typoreporter.web.exception.WorkspaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.hexlet.typoreporter.domain.workspace.AccountRole.ROLE_ANONYMOUS;
import static io.hexlet.typoreporter.domain.workspace.AccountRole.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class WorkspaceRoleService {

    private final WorkspaceRoleRepository repository;

    private final WorkspaceRepository workspaceRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public WorkspaceRole addAccountToWorkspace(String wksName, String accEmail, AccountRole role) {
        final var accId = accountRepository.findAccountByEmail(accEmail)
            .map(Account::getId)
            .orElseThrow(() -> new AccountNotFoundException(accEmail));

        final var wksId = workspaceRepository.getWorkspaceByName(wksName)
            .map(Workspace::getId)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksName));

        final var workspaceRole = new WorkspaceRole(
            new WorkspaceRoleId(wksId, accId),
            role,
            workspaceRepository.getReferenceById(wksId),
            accountRepository.getReferenceById(accId)
        );
        return repository.save(workspaceRole);
    }

    public WorkspaceRole addAccountToWorkspace(String wksName, String accEmail) {
        return addAccountToWorkspace(wksName, accEmail, ROLE_ANONYMOUS);
    }

    public WorkspaceRole addAccountToWorkspaceWhenCreating(String wksName, String accEmail) {
        return addAccountToWorkspace(wksName, accEmail, ROLE_ADMIN);
    }
}
