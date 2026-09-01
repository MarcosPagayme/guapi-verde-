package com.GuapiVerde.mvp.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.ConsentimentoEntrada;
import com.GuapiVerde.mvp.dto.ConsentimentoResponse;
import com.GuapiVerde.mvp.entity.Consentimento;
import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.enums.TipoConsentimento;
import com.GuapiVerde.mvp.exception.DuplicateResourceException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.ConsentimentoRepository;
import com.GuapiVerde.mvp.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsentimentoService {

    private final ConsentimentoRepository consentimentoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<ConsentimentoResponse> listarHistoricoDoUsuario(String email) {
        Usuario usuario = buscarUsuarioAtivo(email);

        return consentimentoRepository
                .findAllByUsuarioIdOrderByDataRegistroDescIdDesc(usuario.getId()).stream()
                .map(ConsentimentoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsentimentoResponse> listarAtuaisDoUsuario(String email) {
        Usuario usuario = buscarUsuarioAtivo(email);

        return Arrays.stream(TipoConsentimento.values())
                .map(tipo -> consentimentoRepository
                        .findFirstByUsuarioIdAndTipoOrderByDataRegistroDescIdDesc(
                                usuario.getId(), tipo))
                .flatMap(java.util.Optional::stream)
                .map(ConsentimentoResponse::de)
                .toList();
    }

    @Transactional
    public ConsentimentoResponse registrar(String email, ConsentimentoEntrada entrada) {
        Usuario usuario = buscarUsuarioAtivo(email);
        String versaoTermo = entrada.versaoTermo().trim();

        consentimentoRepository
                .findFirstByUsuarioIdAndTipoOrderByDataRegistroDescIdDesc(
                        usuario.getId(), entrada.tipo())
                .filter(atual -> atual.getVersaoTermo().equals(versaoTermo)
                        && atual.getConsentido().equals(entrada.consentido()))
                .ifPresent(atual -> {
                    throw new DuplicateResourceException(
                            "Esta decisão de consentimento já está registrada.");
                });

        Consentimento consentimento = new Consentimento(
                usuario,
                entrada.tipo(),
                versaoTermo,
                entrada.consentido());

        return ConsentimentoResponse.de(consentimentoRepository.save(consentimento));
    }

    private Usuario buscarUsuarioAtivo(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .filter(usuario -> Boolean.TRUE.equals(usuario.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado ou desativado."));
    }
}
