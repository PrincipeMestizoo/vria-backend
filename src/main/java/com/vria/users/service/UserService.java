package com.vria.users.service;

import com.vria.users.dto.UserRequestDTO;
import com.vria.users.dto.UserResponseDTO;
import com.vria.users.model.User;
import com.vria.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ---- Requerido por Spring Security para autenticar por email ----
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con el correo: " + email));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = findEntityById(id);
        return toResponse(user);
    }

    public UserResponseDTO create(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo");
        }
        User user = User.builder()
                .name(dto.name())
                .lastName(dto.lastName())
                .enabled(dto.enabled())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(dto.role())
                .build();
        return toResponse(userRepository.save(user));
    }

    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = findEntityById(id);
        user.setName(dto.name());
        user.setLastName(dto.lastName());
        user.setEnabled(dto.enabled());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        return toResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }

    private User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));
    }

    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getIdUser(),
                user.getName(),
                user.getLastName(),
                user.getEnabled(),
                user.getEmail(),
                user.getRole()
        );
    }
}
