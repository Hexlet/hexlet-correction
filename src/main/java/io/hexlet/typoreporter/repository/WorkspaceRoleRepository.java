package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRoleRepository extends JpaRepository<WorkspaceRole, Long> {
    @EntityGraph(attributePaths = {"account"})
    List<WorkspaceRole> getWorkspaceRolesByAccountId(Long accountId);

    @EntityGraph(attributePaths = {"workspace"})
    List<WorkspaceRole> getWorkspaceRolesByWorkspaceId(Long accountId);
}
