package io.hexlet.typoreporter.domain.workspace;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.hexlet.typoreporter.domain.AbstractAuditingEntity;
import io.hexlet.typoreporter.domain.Identifiable;
import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.constraint.WorkspaceDescription;
import io.hexlet.typoreporter.domain.workspace.constraint.WorkspaceName;
import io.hexlet.typoreporter.domain.workspace.constraint.WorkspaceUrl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Workspace extends AbstractAuditingEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workspace_id_seq")
    @SequenceGenerator(name = "workspace_id_seq", allocationSize = 15)
    private Long id;

    @WorkspaceName
    @Column(unique = true)
    private String name;

    @WorkspaceUrl
    @Column(unique = true)
    private String url;

    @WorkspaceDescription
    private String description;

    @NotNull
    private UUID apiAccessToken;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Typo> typos = new HashSet<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Account> accounts = new HashSet<>();

    public Workspace addTypo(final Typo typo) {
        typos.add(typo);
        typo.setWorkspace(this);
        return this;
    }

    public Workspace removeTypo(final Typo typo) {
        typos.remove(typo);
        typo.setWorkspace(null);
        return this;
    }

    public Workspace addAccount(final Account account) {
        accounts.add(account);
        account.setWorkspace(this);
        return this;
    }

    public Workspace removeAccount(final Account account) {
        accounts.remove(account);
        account.setWorkspace(null);
        return this;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || Hibernate.getClass(obj) != Hibernate.getClass(this)) {
            return false;
        }
        Workspace workspace = (Workspace) obj;
        return id != null && Objects.equals(id, workspace.id);
    }
}
