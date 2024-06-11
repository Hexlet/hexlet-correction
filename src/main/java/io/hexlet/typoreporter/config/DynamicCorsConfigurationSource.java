package io.hexlet.typoreporter.config;

import io.hexlet.typoreporter.handler.exception.ForbiddenDomainException;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import io.hexlet.typoreporter.repository.AllowedUrlRepository;
import io.hexlet.typoreporter.utils.TextUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DynamicCorsConfigurationSource implements CorsConfigurationSource {

    private final AllowedUrlRepository urlRepository;
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        String pattern = "/api/workspaces/{id}/typos";

        String referer = request.getHeader("Referer");
        Long wksId = Long.parseLong(matcher.extractUriTemplateVariables(pattern, request.getRequestURI()).get("id"));

        if (wksId != null && referer != null) {
            String refererDomain = TextUtils.trimUrl(referer);
            List<String> allowedUrls = urlRepository.findByWorkspaceId(wksId).stream()
                .map(url -> url.getUrl()).collect(Collectors.toList());

            if (!allowedUrls.isEmpty() && allowedUrls.contains(refererDomain)) {
                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOrigins(allowedUrls);
                config.setAllowedMethods(Arrays.asList("GET", "POST"));
                config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "secretKey"));
                config.setAllowCredentials(true);

                return config;
            } else if (allowedUrls.isEmpty()) {
                throw new WorkspaceNotFoundException(wksId);
            } else {
                throw new ForbiddenDomainException(refererDomain);
            }
        } else if (referer == null) {
            throw new ForbiddenDomainException("null");
        }

        return null;
    }
}
