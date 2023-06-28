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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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
    public DaoAuthenticationProvider accountProvider(AccountDetailService userDetailsService,
                                                     PasswordEncoder pEncoder) {
        final var accountProvider = new DaoAuthenticationProvider();
        accountProvider.setUserDetailsService(userDetailsService);
        accountProvider.setPasswordEncoder(pEncoder);
        return accountProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider apiProvider,
                                                       DaoAuthenticationProvider accountProvider) {
        return new ProviderManager(apiProvider, accountProvider);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
            new RequestAttributeSecurityContextRepository(),
            new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           SecurityContextRepository securityContextRepository) throws Exception {
        http.httpBasic();
        http.cors();
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(GET, "/webjars/**", "/widget/**", "/fragments/**", "/img/**").permitAll()
                .requestMatchers("/", "/login", "/signup", "/error").permitAll()
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

        http.securityContext().securityContextRepository(securityContextRepository);

        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
