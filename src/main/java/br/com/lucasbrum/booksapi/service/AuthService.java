package br.com.lucasbrum.booksapi.service;

import br.com.lucasbrum.booksapi.dto.v1.AuthDtos;

public interface AuthService {
    AuthDtos.LoginResponse login(AuthDtos.LoginRequest request);
    AuthDtos.LoginResponse register(AuthDtos.RegisterRequest request);
}
