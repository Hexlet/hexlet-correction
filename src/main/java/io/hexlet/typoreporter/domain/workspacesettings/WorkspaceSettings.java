package io.hexlet.typoreporter.domain.workspacesettings;

import io.hexlet.typoreporter.domain.AbstractAuditingEntity;
import io.hexlet.typoreporter.domain.Identifiable;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Data
@Accessors(chain = true)
@Entity
public class WorkspaceSettings extends AbstractAuditingEntity implements Identifiable<Long> {

    @Id
    private Long id;

    @NotNull
    private UUID apiAccessToken;

    @MapsId
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Workspace workspace;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof WorkspaceSettings workspaceSettings) {
            return id != null && id.equals(workspaceSettings.id);
        }
        return false;
    }
}
