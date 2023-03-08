package io.hexlet.typoreporter.config;

import io.hexlet.typoreporter.security.service.AccountDetailService;
import io.hexlet.typoreporter.security.service.SecuredWorkspaceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public DaoAuthenticationProvider apiProvider(SecuredWorkspaceService userDetailsService) {
        final var apiProvider = new DaoAuthenticationProvider();
        apiProvider.setUserDetailsService(userDetailsService);
        apiProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return apiProvider;
    }

    @Bean
    public DaoAuthenticationProvider accProvider(AccountDetailService userDetailsService,
                                                 PasswordEncoder pEncoder) {
        final var apiProvider = new DaoAuthenticationProvider();
        apiProvider.setUserDetailsService(userDetailsService);
        apiProvider.setPasswordEncoder(pEncoder);
        return apiProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider apiProvider,
                                                       DaoAuthenticationProvider accProvider) {
        return new ProviderManager(apiProvider, accProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(GET, "/webjars/**", "/widget/**", "/img/**").permitAll()
                .requestMatchers("/", "/workspaces", "/login", "/signup").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/workspaces")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    new AntPathRequestMatcher("/api/**", POST.name()),
                    new AntPathRequestMatcher("/typo/form/*", POST.name())
                )
            );

        http.headers().frameOptions().disable();
        return http.build();
    }
}
