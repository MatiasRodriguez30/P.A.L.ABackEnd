package com.facultad.sistemaavisos.auth;

import com.facultad.sistemaavisos.auth.dto.AuthLoginRequest;
import com.facultad.sistemaavisos.auth.dto.AuthCompleteProfileRequest;
import com.facultad.sistemaavisos.auth.dto.AuthForgotPasswordRequest;
import com.facultad.sistemaavisos.auth.dto.AuthMessageResponse;
import com.facultad.sistemaavisos.auth.dto.AuthRegisterRequest;
import com.facultad.sistemaavisos.auth.dto.AuthRegisterStartResponse;
import com.facultad.sistemaavisos.auth.dto.AuthResetPasswordRequest;
import com.facultad.sistemaavisos.auth.dto.AuthResponse;
import com.facultad.sistemaavisos.auth.dto.AuthVerifyRegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthRegisterStartResponse> register(@RequestBody @Valid AuthRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<AuthResponse> verifyEmail(@RequestBody @Valid AuthVerifyRegistrationRequest request) {
        return ResponseEntity.ok(authService.verifyEmail(request));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<AuthMessageResponse> resendVerification(@RequestBody @Valid AuthForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.resendVerification(request));
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<AuthMessageResponse> forgotPassword(@RequestBody @Valid AuthForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<AuthMessageResponse> resetPassword(@RequestBody @Valid AuthResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }

    @PatchMapping("/perfil-inicial")
    public ResponseEntity<AuthResponse> completarPerfilInicial(
            Authentication authentication,
            @RequestBody @Valid AuthCompleteProfileRequest request
    ) {
        final String bearer = authentication == null ? null : authentication.getCredentials() instanceof String
                ? (String) authentication.getCredentials()
                : null;

        return ResponseEntity.ok(authService.completarPerfilInicial(bearer, request));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication authentication) {
        final String bearer = authentication == null ? null : authentication.getCredentials() instanceof String
                ? (String) authentication.getCredentials()
                : null;
        return ResponseEntity.ok(authService.construirSesionActual(
                bearer,
                authentication != null ? authentication.getName() : null
        ));
    }
}
