package io.hexlet.typoreporter.config;

import io.hexlet.typoreporter.handler.OAuth2SuccessHandler;
import io.hexlet.typoreporter.handler.exception.ForbiddenDomainException;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import io.hexlet.typoreporter.security.service.AccountDetailService;
import io.hexlet.typoreporter.security.service.CustomOAuth2UserService;
import io.hexlet.typoreporter.security.service.SecuredWorkspaceService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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
    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

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
                                           DynamicCorsConfigurationSource dynamicCorsConfigurationSource)
        throws Exception {
        http.httpBasic();
        http.cors();
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(GET, "/webjars/**", "/widget/**", "/fragments/**", "/img/**").permitAll()
                .requestMatchers("/", "/login", "/signup", "/error", "/about").permitAll()
                .requestMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/workspaces")
                .permitAll()
            )
            .oauth2Login(config -> config
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(getOAuth2SuccessHandler())
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    new AntPathRequestMatcher("/api/**", POST.name()),
                    new AntPathRequestMatcher("/typo/form/*", POST.name())
                )
            )
            .addFilterBefore(corsFilter(dynamicCorsConfigurationSource), CorsFilter.class);

        http.securityContext().securityContextRepository(securityContextRepository);

        http.headers().frameOptions().disable();
        return http.build();
    }

    /*@Bean
    public ClientRegistrationRepository getClientRegistrationRepository() {
        return new ClientRegistrationRepository() {
            @Override
            public ClientRegistration findByRegistrationId(String registrationId) {
                return null;
            }
        };
    }*/

    @Bean
    public AuthenticationSuccessHandler getOAuth2SuccessHandler() {
        return new OAuth2SuccessHandler();
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
                } catch (WorkspaceNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
                }
            }
        };
    }
}
