package com.GuapiVerde.mvp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.FavoritoResponse;
import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.entity.Favorito;
import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.exception.DuplicateResourceException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.AtrativoRepository;
import com.GuapiVerde.mvp.repository.FavoritoRepository;
import com.GuapiVerde.mvp.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AtrativoRepository atrativoRepository;

    @Transactional(readOnly = true)
    public List<FavoritoResponse> listarDoUsuario(String email) {
        Usuario usuario = buscarUsuarioAtivo(email);

        return favoritoRepository.findAllByUsuarioIdOrderByDataCadastroDesc(usuario.getId()).stream()
                .map(FavoritoResponse::de)
                .toList();
    }

    @Transactional
    public FavoritoResponse adicionar(String email, Long atrativoId) {
        Usuario usuario = buscarUsuarioAtivo(email);
        Atrativo atrativo = buscarAtrativoAtivo(atrativoId);

        if (favoritoRepository.existsByUsuarioIdAndAtrativoId(usuario.getId(), atrativoId)) {
            throw new DuplicateResourceException("Este atrativo já está nos favoritos do usuário.");
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setAtrativo(atrativo);

        return FavoritoResponse.de(favoritoRepository.save(favorito));
    }

    @Transactional
    public void remover(String email, Long atrativoId) {
        Usuario usuario = buscarUsuarioAtivo(email);
        Favorito favorito = favoritoRepository
                .findByUsuarioIdAndAtrativoId(usuario.getId(), atrativoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Favorito não encontrado para este usuário."));

        favoritoRepository.delete(favorito);
    }

    private Usuario buscarUsuarioAtivo(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .filter(usuario -> Boolean.TRUE.equals(usuario.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado ou desativado."));
    }

    private Atrativo buscarAtrativoAtivo(Long atrativoId) {
        return atrativoRepository.findByIdAndAtivoTrue(atrativoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Atrativo não encontrado ou desativado."));
    }
}
