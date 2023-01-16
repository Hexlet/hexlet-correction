package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRoleId;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.repository.WorkspaceRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceRoleService {

    private final WorkspaceRoleRepository repository;

    private final WorkspaceRepository workspaceRepository;

    private final AccountRepository accountRepository;

    public WorkspaceRole create(Long workspaceId, Long accountId) {
        Account account = accountRepository.findById(accountId).get();
        Workspace workspace = workspaceRepository.findById(workspaceId).get();
        WorkspaceRoleId id = new WorkspaceRoleId(workspaceId, accountId);
        String role = "default_role";
        WorkspaceRole workspaceRole = new WorkspaceRole(id, role, workspace, account);
        return repository.save(workspaceRole);
    }
}
