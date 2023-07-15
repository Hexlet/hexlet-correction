package io.hexlet.typoreporter.domain.account;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.hexlet.typoreporter.domain.AbstractAuditingEntity;
import io.hexlet.typoreporter.domain.Identifiable;
import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Account extends AbstractAuditingEntity implements Identifiable<Long> {

    public static final int PASSWORD_MAX_LENGTH = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", allocationSize = 15)
    private Long id;

    // @NotBlank
    private String externalOpenId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "AUTH_PROVIDER")
    @Type(PostgreSQLEnumType.class)
    private AuthProvider authProvider;

    @Email
    @Column(unique = true)
    private String email;

    @AccountUsername
    @Column(unique = true)
    private String username;

    @NotBlank
    @ToString.Exclude
    private String password;

    @NotBlank
    @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    private String lastName;

    @OneToMany(mappedBy = "account", cascade = ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<WorkspaceRole> workspaceRoles = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Typo> typos = new ArrayList<>();

    public Account addTypo(final Typo typo) {
        typos.add(typo);
        typo.setAccount(this);
        return this;
    }

    public Account removeTypo(final Typo typo) {
        typos.remove(typo);
        typo.setAccount(null);
        return this;
    }

    public Account addWorkspaceRole(WorkspaceRole workspaceRole) {
        workspaceRole.setAccount(this);
        workspaceRoles.add(workspaceRole);
        return this;
    }

    public void removeWorkSpaceRole(WorkspaceRole workspaceRole) {
        workspaceRoles.remove(workspaceRole);
        workspaceRole.setAccount(null);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || id != null && o instanceof Account other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
