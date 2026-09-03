package com.GuapiVerde.mvp.configuration;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.enums.PerfilUsuario;
import com.GuapiVerde.mvp.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder codificadorDeSenha;

    @Value("$configuracao.admin.nome")
    private String nome;

    @Value("${configuracao.admin.email}")
    private String email;

    @Value("${configuracao.admin.senha}")
    private String senha;

    @Override
    public void run(ApplicationArguments argumentos) {
        if (email == null || email.isBlank()
                || senha == null || senha.isBlank()) {

            log.info(
                    "Administrador inicial não configurado. " +
                            "Defina ADMIN_EMAIL e ADMIN_SENHA para criá-lo.");

            return;
        }

        if (senha.length() < 8) {
            throw new IllegalStateException(
                    "A senha do administrador deve possuir pelo menos 8 caracteres.");
        }

        String emailNormalizado = email
                .trim()
                .toLowerCase(Locale.ROOT);

        var usuarioExistente = usuarioRepository.findByEmailIgnoreCase(emailNormalizado);

        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();

            if (usuario.getPerfil() == PerfilUsuario.ADMIN) {
                log.info(
                        "O administrador inicial já está cadastrado.");
            } else {
                log.warn(
                        "O e-mail configurado para o administrador já pertence " +
                                "a um visitante. O perfil não foi alterado automaticamente.");
            }

            return;
        }

        Usuario administrador = new Usuario();
        administrador.setNome(nome.trim());
        administrador.setEmail(emailNormalizado);
        administrador.setSenha(
                codificadorDeSenha.encode(senha));
        administrador.setPerfil(PerfilUsuario.ADMIN);
        administrador.setAtivo(true);

        usuarioRepository.save(administrador);

        log.info(
                "Administrador inicial criado com sucesso: {}",
                emailNormalizado);
    }
}
