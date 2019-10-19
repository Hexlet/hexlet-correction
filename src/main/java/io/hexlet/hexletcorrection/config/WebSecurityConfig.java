package io.hexlet.hexletcorrection.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.API_PATH_V1;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.LOGIN_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.REGISTRATION_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.RESOURCES_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.SWAGGER_UI_PATH;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(REGISTRATION_PATH, LOGIN_PATH, "/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers(RESOURCES_PATH + "/**",
                        API_PATH_V1 + "/**",
                        SWAGGER_UI_PATH + "/**",
                        "/v2/api-docs/**",
                        "/webjars/springfox-swagger-ui/**",
                        "/swagger-resources/**",
                        "/dist/**",
                        "/public/**");
    }
}
