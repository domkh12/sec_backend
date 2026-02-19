package site.secmega.secapi.feature.auth.dto;

import lombok.Builder;

@Builder
public record JwtResponse(
        String tokenType,
        String accessToken
) {
}
