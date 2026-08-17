package site.secmega.secapi.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("No authenticated user found. Using fallback: System");
            return Optional.of("System");
        }

        String username = extractUsername(authentication);
        log.info("Current auditor resolved to: {}", username);
        return Optional.ofNullable(username);
    }

    private String extractUsername(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return getUsernameFromJwt(jwtAuth.getToken());
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return getUsernameFromJwt(jwt);
        }

        String name = authentication.getName();
        if (!"Access Token".equalsIgnoreCase(name) && !"anonymousUser".equalsIgnoreCase(name)) {
            return name;
        }

        return "System";
    }

    private String getUsernameFromJwt(Jwt jwt) {
        // Your token puts the username in "jti"
        String jti = jwt.getClaimAsString("jti");
        if (jti != null && !jti.isBlank()) {
            return jti;
        }

        // Other common claims (just in case)
        String preferred = jwt.getClaimAsString("preferred_username");
        if (preferred != null) return preferred;

        String username = jwt.getClaimAsString("username");
        if (username != null) return username;

        return jwt.getSubject();
    }
}
