package com.GuapiVerde.mvp.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException(
                "Usuário ou senha não encontrados"));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities("ROLE_" + usuario.getPerfil().name())
                .disabled(!usuario.getAtivo())
                .build();
    }
}
