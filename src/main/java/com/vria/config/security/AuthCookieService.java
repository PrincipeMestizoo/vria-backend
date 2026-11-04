package com.vria.config.security;

import com.vria.config.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Construye y lee las cookies HttpOnly que transportan los JWT.
 * Al ser HttpOnly, el frontend no puede leerlas desde JavaScript (protege contra XSS).
 */
@Service
@RequiredArgsConstructor
public class AuthCookieService {

    public static final String ACCESS_TOKEN_COOKIE = "access_token";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    // El refresh token solo se envia a los endpoints de auth, no en cada request
    private static final String ACCESS_TOKEN_PATH = "/";
    private static final String REFRESH_TOKEN_PATH = "/api/v1/auth";

    private final JwtProperties jwtProperties;

    public ResponseCookie accessTokenCookie(String token) {
        return buildCookie(ACCESS_TOKEN_COOKIE, token, ACCESS_TOKEN_PATH,
                Duration.ofMillis(jwtProperties.expirationMs()));
    }

    public ResponseCookie refreshTokenCookie(String token) {
        return buildCookie(REFRESH_TOKEN_COOKIE, token, REFRESH_TOKEN_PATH,
                Duration.ofMillis(jwtProperties.refreshExpirationMs()));
    }

    public ResponseCookie clearAccessTokenCookie() {
        return buildCookie(ACCESS_TOKEN_COOKIE, "", ACCESS_TOKEN_PATH, Duration.ZERO);
    }

    public ResponseCookie clearRefreshTokenCookie() {
        return buildCookie(REFRESH_TOKEN_COOKIE, "", REFRESH_TOKEN_PATH, Duration.ZERO);
    }

    public String readCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private ResponseCookie buildCookie(String name, String value, String path, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(jwtProperties.cookieSecure())
                .sameSite(jwtProperties.cookieSameSite())
                .path(path)
                .maxAge(maxAge)
                .build();
    }
}
