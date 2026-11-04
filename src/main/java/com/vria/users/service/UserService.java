package com.vria.users.service;

import com.vria.users.activity.UserActivityNotifier;
import com.vria.users.dto.UserRequestDTO;
import com.vria.users.dto.UserResponseDTO;
import com.vria.users.mapper.UserMapper;
import com.vria.users.model.User;
import com.vria.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserActivityNotifier activityNotifier;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        return userMapper.toResponse(findEntity(id));
    }

    public UserResponseDTO create(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo");
        }
        User saved = userRepository.save(userMapper.toEntity(dto, passwordEncoder.encode(dto.password())));
        activityNotifier.created(saved);
        return userMapper.toResponse(saved);
    }

    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = findEntity(id);
        userMapper.updateEntity(user, dto);
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        User saved = userRepository.save(user);
        activityNotifier.updated(saved);
        return userMapper.toResponse(saved);
    }

    public void delete(Long id) {
        User user = findEntity(id);
        userRepository.delete(user);
        activityNotifier.deleted(user);
    }

    private User findEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));
    }
}
