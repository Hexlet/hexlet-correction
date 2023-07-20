package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceRoleRepository extends JpaRepository<WorkspaceRole, Long> {

    @EntityGraph(attributePaths = {"account", "workspace"})
    List<WorkspaceRole> getWorkspaceRolesByAccountId(Long accountId);

    @EntityGraph(attributePaths = {"account", "workspace"})
    List<WorkspaceRole> getWorkspaceRolesByAccountUsername(String username);

    @EntityGraph(attributePaths = {"account", "workspace"})
    List<WorkspaceRole> getWorkspaceRolesByWorkspaceId(Long workspaceId);

    @EntityGraph(attributePaths = {"account", "workspace"})
    Optional<WorkspaceRole> getWorkspaceRoleByAccountIdAndWorkspaceId(Long accountId, Long workspaceId);
}
