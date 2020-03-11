package io.hexlet.hexletcorrection.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.hexlet.hexletcorrection.config.Constants.SYSTEM_ACCOUNT;
import static io.hexlet.hexletcorrection.security.SecurityUtils.getCurrentUserLogin;
import static java.util.Optional.of;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return getCurrentUserLogin().or(() -> of(SYSTEM_ACCOUNT));
    }
}
