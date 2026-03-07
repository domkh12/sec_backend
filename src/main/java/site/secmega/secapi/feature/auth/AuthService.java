package site.secmega.secapi.feature.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import site.secmega.secapi.feature.auth.dto.JwtResponse;
import site.secmega.secapi.feature.auth.dto.LoginRequest;
import site.secmega.secapi.feature.auth.dto.ProfileRequest;
import site.secmega.secapi.feature.auth.dto.ProfileResponse;

public interface AuthService {
    ResponseEntity<JwtResponse> login(LoginRequest loginRequest, HttpServletResponse httpServletResponse);

    ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<Void> logout(HttpServletResponse response);

    ProfileResponse getProfile();

    ProfileResponse updateProfile(ProfileRequest profileRequest);
}
