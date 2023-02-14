package io.hexlet.typoreporter.domain.workspace;

import io.hexlet.typoreporter.domain.account.Account;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class WorkspaceRole {

    @EmbeddedId
    private WorkspaceRoleId id;

    @NotNull
    @Enumerated(STRING)
    private AccountRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("workspaceId")
    @ToString.Exclude
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @ToString.Exclude
    private Account account;

    public WorkspaceRole(Workspace workspace, Account account) {
        this.workspace = workspace;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || id != null && o instanceof WorkspaceRole other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
