package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkspaceSettingsRepository extends JpaRepository<WorkspaceSettings, Long> {
    Optional<WorkspaceSettings> getWorkspaceSettingsByWorkspaceId(Long wksId);
}
