package com.GuapiVerde.mvp.service;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.CadastroUsuarioEntrada;
import com.GuapiVerde.mvp.dto.CadastroUsuarioResponse;
import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.enums.PerfilUsuario;
import com.GuapiVerde.mvp.exception.DuplicateResourceException;
import com.GuapiVerde.mvp.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

   @Transactional
    public CadastroUsuarioResponse cadastrarVisitante(
            CadastroUsuarioEntrada entrada
    ) {
        String email = entrada.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException(
                    "Já existe um usuário cadastrado com esse e-mail."
            );
        }

        Usuario usuario = new Usuario();
        usuario.setNome(entrada.nome().trim());
        usuario.setEmail(email);
        usuario.setSenha(
                passwordEncoder.encode(entrada.senha())
        );
        usuario.setPerfil(PerfilUsuario.VISITANTE);
        usuario.setAtivo(true);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return CadastroUsuarioResponse.de(usuarioSalvo);
    }

        @Transactional(readOnly = true)
        public Usuario buscarPorEmail(String email) {
            return usuarioRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new RuntimeException(
                            "Usuário não encontrado com o email: " + email
                    ));
        }
}
