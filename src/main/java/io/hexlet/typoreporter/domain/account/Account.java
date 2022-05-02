package io.hexlet.typoreporter.domain.account;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "pgsql_auth_provider_enum", typeClass = AuthProviderPgEnum.class)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String externalOpenId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_auth_provider_enum")
    @Column(columnDefinition = "AUTH_PROVIDER")
    private AuthProvider authProvider;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 2, max = 20)
    @Pattern(regexp = "A-Za-z0-9_-")
    @Column(unique = true)
    private String username;

    private String password;

    @NotBlank
    @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Typo> typos = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
