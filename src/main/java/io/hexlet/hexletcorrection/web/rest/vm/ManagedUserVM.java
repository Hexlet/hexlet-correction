package io.hexlet.hexletcorrection.web.rest.vm;

import io.hexlet.hexletcorrection.service.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;
}
