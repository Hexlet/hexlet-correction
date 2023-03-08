package io.hexlet.typoreporter.domain.workspacesettings;

import io.hexlet.typoreporter.domain.AbstractAuditingEntity;
import io.hexlet.typoreporter.domain.Identifiable;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

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
