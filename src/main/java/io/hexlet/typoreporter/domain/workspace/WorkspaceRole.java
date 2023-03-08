package io.hexlet.typoreporter.domain.workspace;

import io.hexlet.typoreporter.domain.account.Account;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.EnumType.STRING;

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

    @Override
    public boolean equals(Object o) {
        return this == o || id != null && o instanceof WorkspaceRole other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
