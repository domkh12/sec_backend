package site.secmega.secapi.feature.auth.dto;

public record LoginRequest(
        String username,
        String password
) {
}
