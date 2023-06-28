package io.hexlet.typoreporter.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

public class CookieLocaleResolver extends SessionLocaleResolver {
    private static final String COOKIE_NAME = "lang";

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        super.setLocale(request, response, locale);
        if (response != null) {
            response.addCookie(new Cookie(COOKIE_NAME, locale.getLanguage()));
        }
    }
}
