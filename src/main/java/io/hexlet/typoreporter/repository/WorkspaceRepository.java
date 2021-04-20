package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    Optional<Workspace> getWorkspaceByName(String name);

    boolean existsWorkspaceByName(String name);

    //TODO add tests
    Integer deleteWorkspaceByName(String name);
}
