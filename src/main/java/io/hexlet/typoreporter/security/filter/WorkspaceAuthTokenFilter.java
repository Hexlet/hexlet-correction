package io.hexlet.typoreporter.security.filter;

import io.hexlet.typoreporter.security.authentication.WorkspaceAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpMethod.POST;

@Component
@RequiredArgsConstructor
public class WorkspaceAuthTokenFilter extends OncePerRequestFilter {

    public static final String TOKEN_AUTH_PREFIX = "Token ";

    private final AuthenticationManager manager;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final var requestMatcher = new AntPathRequestMatcher("/api/workspaces/*/typos", POST.name());
        return !requestMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        final var wksName = new AntPathMatcher()
            .extractUriTemplateVariables("/api/workspaces/{wksName}/typos", req.getRequestURI())
            .get("wksName");
        if (wksName == null) {
            return;
        }
        final var authentication = Optional.ofNullable(req.getHeader("Authorization"))
            .filter(header -> header.startsWith(TOKEN_AUTH_PREFIX))
            .map(header -> header.substring(TOKEN_AUTH_PREFIX.length()))
            .map(wksAccessToken -> new WorkspaceAuthenticationToken(wksName, wksAccessToken))
            .map(manager::authenticate);
        if (authentication.isEmpty()) {
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication.get());
        doFilter(req, res, filterChain);
    }
}
