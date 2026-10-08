package com.vria.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vria.security.jwt")
public record JwtProperties(
        String secretKey,
        long expirationMs,
        long refreshExpirationMs,
        boolean cookieSecure,
        String cookieSameSite
) {
}
