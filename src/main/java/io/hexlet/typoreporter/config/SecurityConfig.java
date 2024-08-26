package io.hexlet.typoreporter.config;

import io.hexlet.typoreporter.config.oauth2.GithubConfigurationProperties;
import io.hexlet.typoreporter.handler.OAuth2LoginFailureHandler;
import io.hexlet.typoreporter.handler.OAuth2LoginSuccessHandler;
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
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;
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
    private GithubConfigurationProperties githubConfigurationProperties;

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
    public OAuth2LoginAuthenticationProvider githubProvider(CustomOAuth2UserService customOAuth2UserService) {
        var responseClient = new DefaultAuthorizationCodeTokenResponseClient();
        return new OAuth2LoginAuthenticationProvider(responseClient, customOAuth2UserService);
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider apiProvider,
                                                       DaoAuthenticationProvider accountProvider,
                                                       OAuth2LoginAuthenticationProvider githubProvider
    ) {
        return new ProviderManager(apiProvider, accountProvider, githubProvider);
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
                .requestMatchers("/login/oauth/code/**").permitAll()
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
                .successHandler(getOAuth2LoginSuccessHandler())
                .failureHandler(getOAuth2LoginFailureHandler())
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

    @Bean
    public AuthenticationSuccessHandler getOAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler getOAuth2LoginFailureHandler() {
        return new OAuth2LoginFailureHandler();
    }

    @Bean
    public ClientRegistrationRepository getClientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(githubClientRegistration());
    }

    private ClientRegistration githubClientRegistration() {
        return CommonOAuth2Provider.GITHUB.getBuilder("github")
            .clientId(githubConfigurationProperties.getClientId())
            .clientSecret(githubConfigurationProperties.getClientSecret())
            .scope(githubConfigurationProperties.getScope())
            .build();
    }


    @Bean
    @RequestScope
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
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
