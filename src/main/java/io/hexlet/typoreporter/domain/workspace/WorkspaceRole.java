package io.hexlet.typoreporter.domain.workspace;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hexlet.typoreporter.domain.account.Account;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class WorkspaceRole {

    @EmbeddedId
    WorkspaceRoleId id;

    @ManyToOne
    @MapsId("workspaceId")
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @Override
    public boolean equals(Object o) {
        return this == o || id != null && o instanceof WorkspaceRole other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
