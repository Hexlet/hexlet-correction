package io.hexlet.typoreporter.domain.account;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank
//    private String externalOpenId;
    // pgenum [EMAIL,GITHUB,GOOGLE] / notnull
//    @NotNull
//    @Type()
//    private AuthProvider authProvider;

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
