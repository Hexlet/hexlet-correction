package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.security.model.SecuredWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @EntityGraph(attributePaths = {"workspaceRoles.account"})
    Optional<Workspace> getWorkspaceByName(String wksName);

    Optional<SecuredWorkspace> getSecuredWorkspaceByName(String wksName);

    boolean existsWorkspaceByName(String wksName);

    boolean existsWorkspaceByUrl(String url);

    Integer deleteWorkspaceByName(String wksName);
}
