package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRoleId;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.repository.WorkspaceRoleRepository;
import io.hexlet.typoreporter.handler.exception.AccountNotFoundException;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import io.hexlet.typoreporter.handler.exception.WorkspaceRoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static io.hexlet.typoreporter.domain.workspace.AccountRole.ROLE_ANONYMOUS;

@Service
@RequiredArgsConstructor
public class WorkspaceRoleService {

    private final WorkspaceRoleRepository workspaceRoleRepository;

    private final WorkspaceRepository workspaceRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public WorkspaceRole addAccountToWorkspace(Long wksId, String accEmail) {

        final var accId = accountRepository.findAccountByEmail(accEmail)
            .map(Account::getId)
            .orElseThrow(() -> new AccountNotFoundException(accEmail));

        if (!workspaceRepository.existsWorkspaceById(wksId)) {
            throw new WorkspaceNotFoundException(wksId);
        }

        final var workspaceRole = new WorkspaceRole(
            new WorkspaceRoleId(wksId, accId),
            ROLE_ANONYMOUS,
            workspaceRepository.getReferenceById(wksId),
            accountRepository.getReferenceById(accId)
        );
        return workspaceRoleRepository.save(workspaceRole);
    }

    @Transactional
    public void deleteAccountFromWorkspace(Long wksId, String accountEmail) {
        final var account = accountRepository.findAccountByEmail(accountEmail)
            .orElseThrow(() -> new AccountNotFoundException(accountEmail));
        final var workspace = workspaceRepository.getWorkspaceById(wksId)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksId));
        final var beingDeleteRole = workspaceRoleRepository.getWorkspaceRoleByAccountIdAndWorkspaceId(
                account.getId(),
                wksId)
            .orElseThrow(() -> new WorkspaceRoleNotFoundException(account.getId(), wksId));
        account.removeWorkSpaceRole(beingDeleteRole);
        workspace.removeWorkSpaceRole(beingDeleteRole);
        accountRepository.save(account);
        workspaceRepository.save(workspace);
    }

}
