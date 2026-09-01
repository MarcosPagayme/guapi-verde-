package com.GuapiVerde.mvp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.PreferenciaResponse;
import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import com.GuapiVerde.mvp.entity.Preferencia;
import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.exception.DuplicateResourceException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.CategoriaAtrativoRepository;
import com.GuapiVerde.mvp.repository.PreferenciaRepository;
import com.GuapiVerde.mvp.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenciaService {

    private final PreferenciaRepository preferenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaAtrativoRepository categoriaAtrativoRepository;

    @Transactional(readOnly = true)
    public List<PreferenciaResponse> listarDoUsuario(String email) {
        Usuario usuario = buscarUsuarioAtivo(email);

        return preferenciaRepository
                .findAllByUsuarioIdOrderByCategoriaAtrativoNomeAsc(usuario.getId()).stream()
                .map(PreferenciaResponse::de)
                .toList();
    }

    @Transactional
    public PreferenciaResponse adicionar(String email, Long categoriaAtrativoId) {
        Usuario usuario = buscarUsuarioAtivo(email);
        CategoriaAtrativo categoria = buscarCategoriaAtiva(categoriaAtrativoId);

        if (preferenciaRepository.existsByUsuarioIdAndCategoriaAtrativoId(
                usuario.getId(), categoriaAtrativoId)) {
            throw new DuplicateResourceException(
                    "Esta categoria já está nas preferências do usuário.");
        }

        Preferencia preferencia = new Preferencia();
        preferencia.setUsuario(usuario);
        preferencia.setCategoriaAtrativo(categoria);

        return PreferenciaResponse.de(preferenciaRepository.save(preferencia));
    }

    @Transactional
    public void remover(String email, Long categoriaAtrativoId) {
        Usuario usuario = buscarUsuarioAtivo(email);
        Preferencia preferencia = preferenciaRepository
                .findByUsuarioIdAndCategoriaAtrativoId(usuario.getId(), categoriaAtrativoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Preferência não encontrada para este usuário."));

        preferenciaRepository.delete(preferencia);
    }

    private Usuario buscarUsuarioAtivo(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .filter(usuario -> Boolean.TRUE.equals(usuario.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado ou desativado."));
    }

    private CategoriaAtrativo buscarCategoriaAtiva(Long categoriaAtrativoId) {
        return categoriaAtrativoRepository.findByIdAndAtivoTrue(categoriaAtrativoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria de atrativo não encontrada ou desativada."));
    }
}
