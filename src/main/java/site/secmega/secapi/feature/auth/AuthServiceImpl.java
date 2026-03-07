package site.secmega.secapi.feature.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.auth.dto.JwtResponse;
import site.secmega.secapi.feature.auth.dto.LoginRequest;
import site.secmega.secapi.feature.auth.dto.ProfileRequest;
import site.secmega.secapi.feature.auth.dto.ProfileResponse;
import site.secmega.secapi.feature.user.UserRepository;
import site.secmega.secapi.mapper.UserMapper;
import site.secmega.secapi.util.AuthUtil;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtEncoder jwtEncoder;
    private final AuthUtil authUtil;
    private JwtEncoder jwtEncoderRefreshToken;
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    private UserMapper userMapper;


    @Autowired
    @Qualifier("jwtEncoderRefreshToken")
    public void setJwtEnCoderRefreshToken(JwtEncoder jwtEnCoderRefreshToken){
        this.jwtEncoderRefreshToken = jwtEnCoderRefreshToken;
    }

    @Override
    public ProfileResponse updateProfile(ProfileRequest profileRequest) {
        Long id = authUtil.loggedUserId();
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        userMapper.updateFromProfileRequest(profileRequest, user);
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        return ProfileResponse.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .dateOfBirth(updatedUser.getDateOfBirth())
                .phoneNumber(updatedUser.getPhoneNumber())
                .avatar(updatedUser.getAvatar())
                .role(updatedUser.getRoles().stream().findFirst().orElseThrow().getName())
                .build();
    }

    @Override
    public ProfileResponse getProfile() {
        Long useId = authUtil.loggedUserId();
        User user = userRepository.findById(useId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        return ProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .role(user.getRoles().stream().findFirst().orElseThrow().getName())
                .build();
    }

    @Override
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        String TOKEN_TYPE = "Bearer";
        System.out.println(ResponseCookie.from(TOKEN_TYPE).maxAge(Duration.ofDays(1)));
        // Clear the refresh token cookie
        ResponseCookie clearRefreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefreshTokenCookie.toString());

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        log.info(Arrays.toString(request.getCookies()));
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        log.info("Refresh token: {}", refreshToken);

        if (refreshToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is missing");
        }

        log.info("Refresh token request: {}", refreshToken);
        Authentication auth = new BearerTokenAuthenticationToken(refreshToken);
        auth = jwtAuthenticationProvider.authenticate(auth);

        String scope = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(authority -> !authority.startsWith("FACTOR_"))
                .collect(Collectors.joining(" "));
        ;

        log.info("log4");
        log.info("New Scope: {}", scope);
        log.info("Auth: {}", auth);

        Instant now = Instant.now();

        Jwt jwt = (Jwt) auth.getPrincipal();

        // Create access token claims set
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs", "reactjs"))
                .subject("Access Token")
                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                .claim("scope", scope)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwtClaimsSet);
        Jwt encodedJwt = jwtEncoder.encode(jwtEncoderParameters);

        String accessToken = encodedJwt.getTokenValue();

        if (Duration.between(Instant.now(), jwt.getExpiresAt()).toDays() < 2) {
            // Create refresh token claims set
            JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                    .id(auth.getName())
                    .issuedAt(now)
                    .issuer("web")
                    .audience(List.of("nextjs", "reactjs"))
                    .subject("Refresh Token")
                    .expiresAt(now.plus(7, ChronoUnit.DAYS))
                    .build();
            JwtEncoderParameters jwtEncoderParametersRefreshToken = JwtEncoderParameters.from(jwtClaimsSetRefreshToken);
            Jwt jwtRefreshToken = jwtEncoderRefreshToken.encode(jwtEncoderParametersRefreshToken);
            refreshToken = jwtRefreshToken.getTokenValue();

            // Set new refresh token as an httpOnly cookie
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 days
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        }

        return ResponseEntity.ok(JwtResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .build());
    }

    @Override
    public ResponseEntity<JwtResponse> login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
        );

        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        auth = daoAuthenticationProvider.authenticate(auth);

        // prepare SCOPE
        log.info("Authorities: {}", auth.getAuthorities());

        // ADMIN MANAGER USER

        String scope = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(authority -> !authority.startsWith("FACTOR_"))
                .collect(Collectors.joining(" "));
        ;

        log.info("SCOPE : {}", scope);

        Instant now = Instant.now();

        // Create access token claims set
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs","reactjs"))
                .subject("Access Token")
                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                .claim("scope", scope)
                .build();

        // Create refresh token claims set
        JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs","reactjs"))
                .subject("Refresh Token")
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwtClaimsSet);
        Jwt jwt = jwtEncoder.encode(jwtEncoderParameters);

        JwtEncoderParameters jwtEncoderParametersRefreshToken = JwtEncoderParameters.from(jwtClaimsSetRefreshToken);
        Jwt jwtRefreshToken = jwtEncoderRefreshToken.encode(jwtEncoderParametersRefreshToken);

        String accessToken = jwt.getTokenValue();
        String refreshToken = jwtRefreshToken.getTokenValue();

        // Set refresh token as an httpOnly cookie
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok(JwtResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .build());
    }

    @Autowired
    public void setJwtAuthenticationProvider(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
