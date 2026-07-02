package com.vria.service;

import com.vria.dto.LoginRequest;
import com.vria.dto.LoginResponse;
import com.vria.dto.RegisterRequest;
import com.vria.exception.CodigoDuplicadoException;
import com.vria.model.Usuario;
import com.vria.repository.UsuarioRepository;
import com.vria.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado tras autenticar"));

        String token = jwtService.generarToken(usuario);
        return new LoginResponse(token, usuario.getUsername(), usuario.getRol().name(), jwtService.getExpirationMs());
    }

    /**
     * Registro de usuarios. En un entorno real este endpoint tambien deberia
     * protegerse (solo ADMINISTRADOR), ver ProductoController para el patron.
     */
    public void registrar(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new CodigoDuplicadoException("El username ya esta en uso: " + request.username());
        }

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .nombreCompleto(request.nombreCompleto())
                .rol(request.rol())
                .habilitado(true)
                .build();

        usuarioRepository.save(usuario);
    }
}
