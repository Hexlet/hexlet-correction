package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.security.model.SecuredWorkspace;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    Optional<Workspace> getWorkspaceByName(String name);

    //TODO add tests
    Optional<SecuredWorkspace> getSecuredWorkspaceByName(String name);

    boolean existsWorkspaceByName(String name);

    //TODO add tests
    Integer deleteWorkspaceByName(String name);

    //TODO add tests
    @Modifying
    @Query("update Workspace set apiAccessToken = :token where name = :wksName")
    Integer updateApiAccessTokenByWorkspaceName(String wksName, UUID token);
}
