package com.vria.config;

import com.vria.model.Rol;
import com.vria.model.Usuario;
import com.vria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea un usuario ADMINISTRADOR por defecto la primera vez que la aplicacion
 * arranca con la tabla de usuarios vacia, para poder autenticarse y luego
 * usar /api/auth/registrar y crear al resto del personal (bodegueros, etc).
 *
 * IMPORTANTE: cambiar esta contrasena inmediatamente en un entorno real.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = Usuario.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("Admin123!"))
                    .nombreCompleto("Administrador VRIA")
                    .rol(Rol.ADMINISTRADOR)
                    .habilitado(true)
                    .build();
            usuarioRepository.save(admin);
            log.warn("Usuario admin creado por defecto -> username: admin / password: Admin123! ");
        }
    }
}
