package io.hexlet.typoreporter.domain.account;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.hexlet.typoreporter.domain.AbstractAuditingEntity;
import io.hexlet.typoreporter.domain.Identifiable;
import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@TypeDef(name = "pgsql_auth_provider_enum", typeClass = AuthProviderPgEnum.class)
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
    @Type(type = "pgsql_auth_provider_enum")
    @Column(columnDefinition = "AUTH_PROVIDER")
    private AuthProvider authProvider;

    @Email
    @Column(unique = true)
    private String email;

    @AccountUsername
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Workspace workspace;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
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

    @Override
    public boolean equals(Object o) {
        return this == o || id != null && o instanceof Account other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
