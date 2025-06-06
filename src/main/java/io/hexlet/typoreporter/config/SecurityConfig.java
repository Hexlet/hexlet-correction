package io.hexlet.typoreporter.config;

import io.hexlet.typoreporter.handler.exception.ForbiddenDomainException;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import io.hexlet.typoreporter.security.service.AccountDetailService;
import io.hexlet.typoreporter.security.service.SecuredWorkspaceService;
import io.hexlet.typoreporter.service.oauth2.CustomOAuth2UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;

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
                                   SecurityContextRepository securityContextRepository,
                                   DynamicCorsConfigurationSource dynamicCorsConfigurationSource,
                                           CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http.httpBasic();
        http.cors();
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        http.csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        ).csrf(csrf -> csrf
            .ignoringRequestMatchers(
                "/login/oauth2/code/**",
                "/oauth2/authorization/**")
        );

        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(GET, "/webjars/**", "/widget/**", "/fragments/**", "/img/**",
                    "/favicon.ico").permitAll()
                .requestMatchers("/", "/login", "/signup", "/error", "/about", "/oauth2/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/workspaces")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    new AntPathRequestMatcher("/api/**", POST.name()),
                    new AntPathRequestMatcher("/typo/form/*", POST.name())
                )
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                .defaultSuccessUrl("/workspaces", true)
            )
            .addFilterBefore(corsFilter(dynamicCorsConfigurationSource), CorsFilter.class);

        http.securityContext().securityContextRepository(securityContextRepository);

        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CorsFilter corsFilter(DynamicCorsConfigurationSource dynamicCorsConfigurationSource) {
        return new CorsFilter(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                if (request.getRequestURI().startsWith("/api/workspaces/")) {
                    CorsConfiguration config = dynamicCorsConfigurationSource.getCorsConfiguration(request);

                    return config;
                }
                return null;
            }

        }) {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain)
                throws ServletException, IOException {
                try {
                    super.doFilterInternal(request, response, filterChain);
                } catch (ForbiddenDomainException e) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                }  catch (WorkspaceNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
                }
            }
        };
    }
}
