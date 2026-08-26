package com.GuapiVerde.mvp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.LoginEntrada;
import com.GuapiVerde.mvp.dto.LoginResponse;
import com.GuapiVerde.mvp.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginEntrada entrada) {
        return authService.login(entrada);
    }
    
}
