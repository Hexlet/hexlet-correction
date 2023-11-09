package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    @EntityGraph(attributePaths = {"workspaceRoles.account"})
    Optional<Workspace> getWorkspaceById(Long wksId);

    boolean existsWorkspaceByName(String wksName);

    boolean existsWorkspaceById(Long wksId);

    boolean existsWorkspaceByUrl(String url);

    Integer deleteWorkspaceById(Long wksId);
}
