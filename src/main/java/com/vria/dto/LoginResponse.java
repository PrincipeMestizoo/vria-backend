package com.vria.dto;

public record LoginResponse(
        String token,
        String tokenType,
        String username,
        String rol,
        long expiraEnMs
) {
    public LoginResponse(String token, String username, String rol, long expiraEnMs) {
        this(token, "Bearer", username, rol, expiraEnMs);
    }
}
