package com.gabrielyorlando.locacao.controllers;

import com.gabrielyorlando.locacao.models.dtos.auth.LoginResponse;
import com.gabrielyorlando.locacao.models.dtos.auth.LoginRequest;
import com.gabrielyorlando.locacao.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        String token = authService.authenticate(authentication);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    //TODO: NO FLYWAY CRIAR USUARIO PADRAO COM SENHA CRIPTOGRAFADA
}
