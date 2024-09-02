package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-01T23:33:48+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class WorkspaceMapperImpl implements WorkspaceMapper {

    @Override
    public Workspace toWorkspace(CreateWorkspace source) {
        if ( source == null ) {
            return null;
        }

        Workspace workspace = new Workspace();

        workspace.setName( source.name() );
        workspace.setUrl( source.url() );
        workspace.setDescription( source.description() );

        return workspace;
    }

    @Override
    public WorkspaceInfo toWorkspaceInfo(Workspace source) {
        if ( source == null ) {
            return null;
        }

        String createdDateAgo = null;
        String modifiedDateAgo = null;
        Long id = null;
        String name = null;
        String url = null;
        String description = null;
        String createdBy = null;
        Instant createdDate = null;
        String modifiedBy = null;
        Instant modifiedDate = null;

        createdDateAgo = getDateAgoAsString( source.getCreatedDate() );
        modifiedDateAgo = getDateAgoAsString( source.getModifiedDate() );
        id = source.getId();
        name = source.getName();
        url = source.getUrl();
        description = source.getDescription();
        createdBy = source.getCreatedBy();
        createdDate = source.getCreatedDate();
        modifiedBy = source.getModifiedBy();
        modifiedDate = source.getModifiedDate();

        WorkspaceInfo workspaceInfo = new WorkspaceInfo( id, name, url, description, createdBy, createdDate, createdDateAgo, modifiedBy, modifiedDate, modifiedDateAgo );

        return workspaceInfo;
    }
}
