package com.GuapiVerde.mvp.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.ImagemAtrativoEntrada;
import com.GuapiVerde.mvp.dto.ImagemAtrativoResponse;
import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.entity.ImagemAtrativo;
import com.GuapiVerde.mvp.exception.RegraDeNegocioException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.AtrativoRepository;
import com.GuapiVerde.mvp.repository.ImagemAtrativoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImagemAtrativoService {

    private final ImagemAtrativoRepository repositorio;
    private final AtrativoRepository atrativoRepositorio;

    @Transactional(readOnly = true)
    public List<ImagemAtrativoResponse> listar() {
        return repositorio.findAllByOrderByAtrativoNomeAscOrdemAscIdAsc().stream()
                .map(ImagemAtrativoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public ImagemAtrativoResponse obterPorId(Long id) {
        return ImagemAtrativoResponse.de(buscarImagem(id));
    }

    @Transactional(readOnly = true)
    public List<ImagemAtrativoResponse> listarPorAtrativo(Long atrativoId) {
        buscarAtrativoAtivo(atrativoId);

        return repositorio.findAllByAtrativoIdOrderByOrdemAscIdAsc(atrativoId).stream()
                .map(ImagemAtrativoResponse::de)
                .toList();
    }

    @Transactional
    public ImagemAtrativoResponse cadastrar(ImagemAtrativoEntrada entrada) {
        Atrativo atrativo = buscarAtrativoAtivo(entrada.atrativoId());
        String url = normalizarEValidarUrl(entrada.url());
        String textoAlternativo = normalizarEValidarTextoAlternativo(entrada.textoAlternativo());
        validarOrdem(entrada.ordem());

        ImagemAtrativo imagem = new ImagemAtrativo();

        if (Boolean.TRUE.equals(entrada.principal())) {
            desmarcarOutrasImagensPrincipais(atrativo.getId(), null);
        }

        preencher(imagem, entrada, atrativo, url, textoAlternativo);

        return ImagemAtrativoResponse.de(repositorio.save(imagem));
    }

    @Transactional
    public ImagemAtrativoResponse atualizar(Long id, ImagemAtrativoEntrada entrada) {
        ImagemAtrativo imagem = buscarImagem(id);
        Atrativo atrativo = buscarAtrativoAtivo(entrada.atrativoId());
        String url = normalizarEValidarUrl(entrada.url());
        String textoAlternativo = normalizarEValidarTextoAlternativo(entrada.textoAlternativo());
        validarOrdem(entrada.ordem());

        if (Boolean.TRUE.equals(entrada.principal())) {
            desmarcarOutrasImagensPrincipais(atrativo.getId(), imagem.getId());
        }

        preencher(imagem, entrada, atrativo, url, textoAlternativo);

        return ImagemAtrativoResponse.de(repositorio.save(imagem));
    }

    @Transactional
    public void excluir(Long id) {
        ImagemAtrativo imagem = buscarImagem(id);
        repositorio.delete(imagem);
    }

    private ImagemAtrativo buscarImagem(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Imagem de atrativo não encontrada."));
    }

    private Atrativo buscarAtrativoAtivo(Long atrativoId) {
        return atrativoRepositorio.findByIdAndAtivoTrue(atrativoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Atrativo não encontrado ou desativado."));
    }

    private void desmarcarOutrasImagensPrincipais(Long atrativoId, Long imagemAtualId) {
        repositorio.findAllByAtrativoIdAndPrincipalTrue(atrativoId).stream()
                .filter(imagem -> !Objects.equals(imagem.getId(), imagemAtualId))
                .forEach(imagem -> imagem.setPrincipal(false));
    }

    private String normalizarEValidarUrl(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new RegraDeNegocioException("A URL da imagem é obrigatória.");
        }

        String url = valor.trim();

        try {
            URI uri = new URI(url);
            String esquema = uri.getScheme();

            if (uri.getHost() == null
                    || !("http".equalsIgnoreCase(esquema) || "https".equalsIgnoreCase(esquema))) {
                throw new RegraDeNegocioException(
                        "A URL da imagem deve ser uma URL HTTP ou HTTPS válida.");
            }
        } catch (URISyntaxException excecao) {
            throw new RegraDeNegocioException(
                    "A URL da imagem deve ser uma URL HTTP ou HTTPS válida.");
        }

        return url;
    }

    private String normalizarEValidarTextoAlternativo(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new RegraDeNegocioException("O texto alternativo é obrigatório.");
        }

        return valor.trim();
    }

    private void validarOrdem(Integer ordem) {
        if (ordem == null || ordem < 0) {
            throw new RegraDeNegocioException("A ordem não pode ser negativa.");
        }
    }

    private void preencher(
            ImagemAtrativo imagem,
            ImagemAtrativoEntrada entrada,
            Atrativo atrativo,
            String url,
            String textoAlternativo) {
        imagem.setAtrativo(atrativo);
        imagem.setUrl(url);
        imagem.setTextoAlternativo(textoAlternativo);
        imagem.setPrincipal(entrada.principal());
        imagem.setOrdem(entrada.ordem());
    }
}
