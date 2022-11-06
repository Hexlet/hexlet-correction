package io.hexlet.typoreporter.config;

import io.hexlet.typoreporter.domain.role.Role;
import io.hexlet.typoreporter.security.filter.WorkspaceAuthTokenFilter;
import io.hexlet.typoreporter.security.provider.AccountAuthenticationProvider;
import io.hexlet.typoreporter.security.provider.WorkspaceTokenAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.zalando.problem.spring.web.advice.AdviceTrait;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import static io.hexlet.typoreporter.web.Routers.CREATE;
import static io.hexlet.typoreporter.web.Routers.SETTINGS;
import static io.hexlet.typoreporter.web.Routers.Typo.TYPOS;
import static io.hexlet.typoreporter.web.Routers.Workspace.API_WORKSPACES;
import static io.hexlet.typoreporter.web.Routers.Workspace.WKS_NAME_PATH;
import static io.hexlet.typoreporter.web.Routers.Workspace.WORKSPACE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProblemSupport problemSupport;

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(AdviceTrait.class)
    public AdviceTrait securityExceptionHandling() {
        return new SecurityExceptionHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager(WorkspaceTokenAuthenticationProvider wksProvider,
                                                       AccountAuthenticationProvider accProvider) {
        return new ProviderManager(wksProvider, accProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http.httpBasic();

        http.exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport);

        http.authorizeRequests()
            .antMatchers(GET, "/", "/webjars/**", "/static/**").permitAll()

            .mvcMatchers(POST, API_WORKSPACES + "/*" + TYPOS).authenticated()

            .antMatchers(CREATE + WORKSPACE + "/**").hasAuthority("CREATOR")
            .antMatchers(WORKSPACE + WKS_NAME_PATH + "/**").hasAnyAuthority("EDITOR", "CREATOR")
            .antMatchers(GET, WORKSPACE + WKS_NAME_PATH + "/**")
            .hasAnyAuthority("WATCHER", "EDITOR", "CREATOR")
            .antMatchers(DELETE, WORKSPACE + WKS_NAME_PATH).hasAnyAuthority("CREATOR")
            .antMatchers(WORKSPACE + WKS_NAME_PATH + SETTINGS + "/**").hasAuthority("CREATOR")
            .antMatchers(PATCH, WORKSPACE + WKS_NAME_PATH + "/token/regenerate").hasAuthority("CREATOR")

            .antMatchers(TYPOS + "/**").hasAnyAuthority("EDITOR", "CREATOR")

            .and()
            .formLogin().permitAll()
            .and()
            .csrf()
            .ignoringRequestMatchers(new AntPathRequestMatcher(API_WORKSPACES + "/*" + TYPOS, POST.name()));

        http.addFilterBefore(new WorkspaceAuthTokenFilter(authManager), BasicAuthenticationFilter.class);

        return http.build();
    }
}
