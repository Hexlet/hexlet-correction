package io.hexlet.typoreporter.config.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.security.Principal;
import java.util.Optional;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Configuration
@EnableJpaAuditing
public class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(getContext().getAuthentication()).map(Principal::getName);
    }
}
