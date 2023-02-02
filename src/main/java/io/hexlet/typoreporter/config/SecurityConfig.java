package io.hexlet.typoreporter.config;

import io.hexlet.typoreporter.security.filter.WorkspaceAuthTokenFilter;
import io.hexlet.typoreporter.security.provider.AccountAuthenticationProvider;
import io.hexlet.typoreporter.security.provider.WorkspaceTokenAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static io.hexlet.typoreporter.web.Routers.Typo.TYPOS;
import static io.hexlet.typoreporter.web.Routers.Workspace.API_WORKSPACES;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(WorkspaceTokenAuthenticationProvider wksProvider,
                                                       AccountAuthenticationProvider accProvider) {
        return new ProviderManager(wksProvider, accProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http.httpBasic();

        http.authorizeHttpRequests()
            .requestMatchers(GET, "/", "/workspaces", "/webjars/**", "/static/**").permitAll()
            .requestMatchers(POST, API_WORKSPACES + "/*" + TYPOS).authenticated()
            .requestMatchers("/workspace/**", "/create/workspace").authenticated()
            .requestMatchers("/account/**").authenticated()
            .and()
            .formLogin()
            .defaultSuccessUrl("/workspaces")
            .permitAll()
            .and()
            .csrf()
            .ignoringRequestMatchers(
                new AntPathRequestMatcher(API_WORKSPACES + "/*" + TYPOS, POST.name()),
                new AntPathRequestMatcher("/typo/form/*", POST.name())
            );

        http.addFilterBefore(new WorkspaceAuthTokenFilter(authManager), BasicAuthenticationFilter.class);

        http.headers().frameOptions().disable();
        return http.build();
    }
}
