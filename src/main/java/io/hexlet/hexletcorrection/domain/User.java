package io.hexlet.hexletcorrection.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name not be null")
    @NotBlank(message = "Name not be empty")
    @Size(message = "Name not be more than 50 characters", max = 50)
    private String name;

    @NotNull(message = "Email not be null")
    @NotBlank(message = "Email not be empty")
    @Email(message="Invalid Email provided")
    private String email;

    @OneToMany(mappedBy = "user")
    private Set<Correction> corrections;
}
