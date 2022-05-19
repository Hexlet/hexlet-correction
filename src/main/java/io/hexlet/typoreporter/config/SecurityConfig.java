package io.hexlet.typoreporter.config;

import io.hexlet.typoreporter.security.filter.WorkspaceAuthTokenFilter;
import io.hexlet.typoreporter.security.provider.WorkspaceTokenAuthenticationProvider;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.zalando.problem.spring.web.advice.AdviceTrait;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import static io.hexlet.typoreporter.web.Routers.Typo.TYPOS;
import static io.hexlet.typoreporter.web.Routers.Workspace.API_WORKSPACES;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@Setter(onMethod_ = @Autowired)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private SecurityProblemSupport problemSupport;

    private WorkspaceTokenAuthenticationProvider workspaceTokenAuthenticationProvider;

    @Bean
    public PasswordEncoder noOpPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(AdviceTrait.class)
    public AdviceTrait securityExceptionHandling() {
        return new SecurityExceptionHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(workspaceTokenAuthenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport);

        http.authorizeRequests()
            .mvcMatchers(POST, API_WORKSPACES + "/*" + TYPOS).authenticated()
            .anyRequest().permitAll()
            .and()
            .csrf()
            .ignoringRequestMatchers(new AntPathRequestMatcher(API_WORKSPACES + "/*" + TYPOS, POST.name()));

        http.addFilterBefore(new WorkspaceAuthTokenFilter(authenticationManagerBean()), BasicAuthenticationFilter.class);
    }
}
