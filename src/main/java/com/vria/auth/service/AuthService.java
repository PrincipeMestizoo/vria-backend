package com.vria.auth.service;

import com.vria.auth.dto.AuthSession;
import com.vria.auth.dto.LoginRequest;
import com.vria.auth.dto.RegisterRequest;
import com.vria.auth.mapper.AuthMapper;
import com.vria.config.security.JwtService;
import com.vria.users.model.User;
import com.vria.users.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;

    @Transactional
    public AuthSession register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese correo");
        }
        User user = userRepository.save(authMapper.toEntity(request, passwordEncoder.encode(request.password())));
        return buildAuthSession(user);
    }

    @Transactional(readOnly = true)
    public AuthSession login(LoginRequest request) {
        // Delega la validacion de credenciales en el AuthenticationManager
        // (usa DaoAuthenticationProvider -> EmailUserDetailsService + PasswordEncoder)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return buildAuthSession(user);
    }

    /**
     * Valida el refresh token y emite un par nuevo (rotacion de refresh token).
     */
    @Transactional(readOnly = true)
    public AuthSession refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token ausente");
        }
        try {
            String email = jwtService.extractUsername(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

            if (!user.isEnabled() || !jwtService.isRefreshTokenValid(refreshToken, user)) {
                throw new BadCredentialsException("Refresh token invalido");
            }
            return buildAuthSession(user);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Refresh token invalido", ex);
        }
    }

    private AuthSession buildAuthSession(User user) {
        return new AuthSession(
                jwtService.generateToken(user),
                jwtService.generateRefreshToken(user),
                authMapper.toAuthResponse(user)
        );
    }
}
