package io.hexlet.typoreporter.annotation;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithOAuth2Login;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithOAuth2Login(authorities = {"read:user", "email:user"},
    claims = @OpenIdClaims(
        emailVerified = true,
        email = "test@gmail.com",
        middleName = "Тестеров",
        name = "Тест",
        nickName = "test"

    ),
    authorizedClientRegistrationId = "github")
public @interface WithCustomOAuth2 {
}
