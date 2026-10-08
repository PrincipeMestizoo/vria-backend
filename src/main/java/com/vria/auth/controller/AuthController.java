package com.vria.auth.controller;

import com.vria.auth.dto.AuthResponse;
import com.vria.auth.dto.AuthSession;
import com.vria.auth.dto.LoginRequest;
import com.vria.auth.dto.RegisterRequest;
import com.vria.auth.service.AuthService;
import com.vria.config.security.AuthCookieService;
import com.vria.users.model.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de autenticacion del sistema VRIA.
 * Los JWT se envian al cliente como cookies HttpOnly; el body solo contiene datos del usuario.
 * register, login, refresh y logout son publicos (ver SecurityConfig -> PUBLIC_ENDPOINTS).
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Registro e inicio de sesion (JWT en cookies HttpOnly)")
@SecurityRequirements // sobreescribe el requisito global de bearerAuth para este controller
public class AuthController {

    private final AuthService authService;
    private final AuthCookieService authCookieService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return withSessionCookies(HttpStatus.CREATED, authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return withSessionCookies(HttpStatus.OK, authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request) {
        String refreshToken = authCookieService.readCookie(request, AuthCookieService.REFRESH_TOKEN_COOKIE);
        return withSessionCookies(HttpStatus.OK, authService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, authCookieService.clearAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, authCookieService.clearRefreshTokenCookie().toString())
                .build();
    }

    /**
     * Devuelve el usuario de la sesion actual. El frontend lo usa al recargar la pagina
     * en lugar de guardar los datos del usuario en localStorage.
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(authService.toAuthResponse(user));
    }

    private ResponseEntity<AuthResponse> withSessionCookies(HttpStatus status, AuthSession session) {
        return ResponseEntity.status(status)
                .header(HttpHeaders.SET_COOKIE, authCookieService.accessTokenCookie(session.accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, authCookieService.refreshTokenCookie(session.refreshToken()).toString())
                .body(session.user());
    }
}
