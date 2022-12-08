package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceSettingsRepository extends JpaRepository<WorkspaceSettings, Long> {

    @Modifying
    @Query("update WorkspaceSettings set apiAccessToken = :token where workspace.id in (select id from Workspace where name = :wksName)")
    Integer updateApiAccessTokenByWorkspaceName(String wksName, UUID token);

    @Query("select apiAccessToken from WorkspaceSettings where workspace.id in (select id from Workspace where name = :wksName)")
    Optional<UUID> getApiAccessTokenByWorkspaceName(String wksName);
}
