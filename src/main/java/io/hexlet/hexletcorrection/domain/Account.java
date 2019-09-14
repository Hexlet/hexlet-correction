package io.hexlet.hexletcorrection.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.INVALID_EMAIL;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.MAX_ACCOUNT_NAME;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_EMPTY;
import static javax.persistence.FetchType.EAGER;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name " + NOT_EMPTY)
    @Size(message = "Name not be more than " + MAX_ACCOUNT_NAME + " characters", max = MAX_ACCOUNT_NAME)
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Email " + NOT_EMPTY)
    @Email(message = INVALID_EMAIL)
    private String email;

    @OneToMany(mappedBy = "account", fetch = EAGER)
    @JsonIgnoreProperties("account")
    private Set<Correction> corrections = new HashSet<>();
}
