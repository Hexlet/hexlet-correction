package io.hexlet.typoreporter.config;

import io.hexlet.typoreporter.security.filter.WorkspaceAuthTokenFilter;
import io.hexlet.typoreporter.security.provider.AccountAuthenticationProvider;
import io.hexlet.typoreporter.security.provider.WorkspaceTokenAuthenticationProvider;
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

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;


@Configuration
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

        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(GET, "/webjars/**", "/widget/**", "/img/**").permitAll()
                .requestMatchers("/", "/workspaces", "/login", "/logout").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/workspaces")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    new AntPathRequestMatcher("/api/workspaces/*/typos", POST.name()),
                    new AntPathRequestMatcher("/typo/form/*", POST.name())
                )
            );

        http.addFilterBefore(new WorkspaceAuthTokenFilter(authManager), BasicAuthenticationFilter.class);

        http.headers().frameOptions().disable();
        return http.build();
    }
}
