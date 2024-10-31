package io.hexlet.typoreporter.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity(name = "account_social_links")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AccountSocialLink implements Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "social_links_to_accounts_id_seq")
    @SequenceGenerator(name = "social_links_to_accounts_id_seq", allocationSize = 15)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @NotNull
    private Account account;

    @Column(name = "login")
    @NotNull
    private Integer login;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "AUTH_PROVIDER")
    @Type(PostgreSQLEnumType.class)
    private AuthProvider authProvider;
}
