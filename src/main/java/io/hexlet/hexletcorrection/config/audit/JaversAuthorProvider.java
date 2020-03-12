package io.hexlet.hexletcorrection.config.audit;

import io.hexlet.hexletcorrection.config.Constants;
import io.hexlet.hexletcorrection.security.SecurityUtils;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.stereotype.Component;

@Component
public class JaversAuthorProvider implements AuthorProvider {

    @Override
    public String provide() {
        return SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT);
    }
}
