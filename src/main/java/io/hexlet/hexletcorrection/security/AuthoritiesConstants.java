package io.hexlet.hexletcorrection.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String CORRECTER = "ROLE_CORRECTER";

    public static final String RESOLVER = "ROLE_RESOLVER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
