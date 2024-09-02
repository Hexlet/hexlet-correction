package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.workspace.AccountRole;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceRoleInfo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-01T23:33:48+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class WorkspaceRoleMapperImpl implements WorkspaceRoleMapper {

    @Override
    public WorkspaceRoleInfo toWorkspaceRoleInfo(WorkspaceRole source) {
        if ( source == null ) {
            return null;
        }

        Long workspaceId = null;
        Long accountId = null;
        String workspaceUrl = null;
        String workspaceDescription = null;
        String workspaceName = null;
        String username = null;
        AccountRole role = null;

        workspaceId = sourceWorkspaceId( source );
        accountId = sourceAccountId( source );
        workspaceUrl = sourceWorkspaceUrl( source );
        workspaceDescription = sourceWorkspaceDescription( source );
        workspaceName = sourceWorkspaceName( source );
        username = sourceAccountUsername( source );
        role = source.getRole();

        WorkspaceRoleInfo workspaceRoleInfo = new WorkspaceRoleInfo( workspaceId, accountId, workspaceName, workspaceUrl, workspaceDescription, username, role );

        return workspaceRoleInfo;
    }

    private Long sourceWorkspaceId(WorkspaceRole workspaceRole) {
        if ( workspaceRole == null ) {
            return null;
        }
        Workspace workspace = workspaceRole.getWorkspace();
        if ( workspace == null ) {
            return null;
        }
        Long id = workspace.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long sourceAccountId(WorkspaceRole workspaceRole) {
        if ( workspaceRole == null ) {
            return null;
        }
        Account account = workspaceRole.getAccount();
        if ( account == null ) {
            return null;
        }
        Long id = account.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String sourceWorkspaceUrl(WorkspaceRole workspaceRole) {
        if ( workspaceRole == null ) {
            return null;
        }
        Workspace workspace = workspaceRole.getWorkspace();
        if ( workspace == null ) {
            return null;
        }
        String url = workspace.getUrl();
        if ( url == null ) {
            return null;
        }
        return url;
    }

    private String sourceWorkspaceDescription(WorkspaceRole workspaceRole) {
        if ( workspaceRole == null ) {
            return null;
        }
        Workspace workspace = workspaceRole.getWorkspace();
        if ( workspace == null ) {
            return null;
        }
        String description = workspace.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }

    private String sourceWorkspaceName(WorkspaceRole workspaceRole) {
        if ( workspaceRole == null ) {
            return null;
        }
        Workspace workspace = workspaceRole.getWorkspace();
        if ( workspace == null ) {
            return null;
        }
        String name = workspace.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String sourceAccountUsername(WorkspaceRole workspaceRole) {
        if ( workspaceRole == null ) {
            return null;
        }
        Account account = workspaceRole.getAccount();
        if ( account == null ) {
            return null;
        }
        String username = account.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
