package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
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

@Service
@RequiredArgsConstructor
public class WorkspaceRoleService {

    private final WorkspaceRoleRepository repository;

    private final WorkspaceRepository workspaceRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public WorkspaceRole addAccountToWorkspace(String wksName, String accEmail) {
        final var accId = accountRepository.findAccountByEmail(accEmail)
            .map(Account::getId)
            .orElseThrow(() -> new AccountNotFoundException(accEmail));

        final var wksId = workspaceRepository.getWorkspaceByName(wksName)
            .map(Workspace::getId)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksName));

        final var workspaceRole = new WorkspaceRole(
            new WorkspaceRoleId(wksId, accId),
            "default_role",
            workspaceRepository.getReferenceById(wksId),
            accountRepository.getReferenceById(accId)
        );
        return repository.save(workspaceRole);
    }
}
