package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.security.model.SecuredWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @EntityGraph(attributePaths = {"accounts.account"})
    Optional<Workspace> getWorkspaceByName(String name);

    Optional<SecuredWorkspace> getSecuredWorkspaceByName(String name);

    boolean existsWorkspaceByName(String name);

    boolean existsWorkspaceByUrl(String url);

    Integer deleteWorkspaceByName(String name);

    @Modifying
    @Query("update Workspace set apiAccessToken = :token where name = :wksName")
    Integer updateApiAccessTokenByWorkspaceName(String wksName, UUID token);
}
