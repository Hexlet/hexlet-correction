package io.hexlet.typoreporter.domain.workspace;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class WorkspaceRoleId implements Serializable {

    @Column(name = "workspace_id")
    private Long workspaceId;

    @Column(name = "account_id")
    private Long accountId;

    @Override
    public boolean equals(Object o) {
        return (this == o || workspaceId != null && o instanceof WorkspaceRole other && workspaceId.equals(other.id))
                && (this == o || accountId != null && o instanceof WorkspaceRole other && accountId.equals(other.id));
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
