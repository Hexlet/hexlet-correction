package io.hexlet.typoreporter.domain.workspace;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@ToString
public class WorkspaceRoleId implements Serializable {

    private Long workspaceId;

    private Long accountId;

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof WorkspaceRoleId other) {
            return workspaceId.equals(other.workspaceId) && accountId.equals(other.accountId);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(workspaceId, accountId);
    }
}
