package com.moneyroomba.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    // Ver aca: https://www.jhipster.tech/tips/025_tip_create_new_authority.html
    // Modificar authority.csv
    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_REGULAR_USER";

    public static final String PREMIUM_USER = "ROLE_PREMIUM_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
