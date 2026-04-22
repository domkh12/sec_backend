package site.secmega.secapi.feature.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.auth.dto.JwtResponse;
import site.secmega.secapi.feature.auth.dto.LoginRequest;
import site.secmega.secapi.feature.auth.dto.ProfileRequest;
import site.secmega.secapi.feature.auth.dto.ProfileResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_VIEWER', 'ROLE_WAREHOUSE')")
    @PutMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    ProfileResponse updateProfile(@RequestBody ProfileRequest profileRequest) throws IOException {
        return authService.updateProfile(profileRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_VIEWER', 'ROLE_WAREHOUSE')")
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    ProfileResponse getProfile(){
        return authService.getProfile();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> logout(HttpServletResponse response) {
        return authService.logout(response);
    }

    @GetMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response){
        return authService.refreshToken(request, response);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse){
        return authService.login(loginRequest, httpServletResponse);
    }
}
