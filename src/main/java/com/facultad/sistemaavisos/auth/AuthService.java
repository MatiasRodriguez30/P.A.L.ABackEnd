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

public interface AuthService {

    AuthResponse login(AuthLoginRequest request);

    AuthRegisterStartResponse register(AuthRegisterRequest request);

    AuthResponse verifyEmail(AuthVerifyRegistrationRequest request);

    AuthMessageResponse resendVerification(AuthForgotPasswordRequest request);

    AuthMessageResponse forgotPassword(AuthForgotPasswordRequest request);

    AuthMessageResponse resetPassword(AuthResetPasswordRequest request);

    AuthResponse completarPerfilInicial(String bearerToken, AuthCompleteProfileRequest request);

    AuthResponse construirSesionActual(String bearerToken, String mailUsuario);
}
