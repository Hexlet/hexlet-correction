package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRoleRepository extends JpaRepository<WorkspaceRole, Long> {

}
