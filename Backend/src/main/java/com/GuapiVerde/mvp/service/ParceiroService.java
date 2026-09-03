package com.GuapiVerde.mvp.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.ParceiroEntrada;
import com.GuapiVerde.mvp.dto.ParceiroResponse;
import com.GuapiVerde.mvp.entity.Parceiro;
import com.GuapiVerde.mvp.exception.RegraDeNegocioException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.ParceiroRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParceiroService {

    private final ParceiroRepository repositorio;

    @Transactional(readOnly = true)
    public List<ParceiroResponse> listarPublicados() {
        return converterLista(repositorio.findAllByAtivoTrueOrderByNomeAsc());
    }

    @Transactional(readOnly = true)
    public ParceiroResponse obterPublicadoPorId(Long id) {
        Parceiro parceiro = repositorio.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parceiro não encontrado ou desativado."));

        return ParceiroResponse.de(parceiro);
    }

    @Transactional(readOnly = true)
    public List<ParceiroResponse> listarParaAdministracao() {
        return converterLista(repositorio.findAllByOrderByNomeAsc());
    }

    @Transactional(readOnly = true)
    public ParceiroResponse obterParaAdministracaoPorId(Long id) {
        return ParceiroResponse.de(buscarPorId(id));
    }

    @Transactional
    public ParceiroResponse cadastrar(ParceiroEntrada entrada) {
        Parceiro parceiro = new Parceiro();
        preencher(parceiro, entrada);
        parceiro.setAtivo(true);

        return ParceiroResponse.de(repositorio.save(parceiro));
    }

    @Transactional
    public ParceiroResponse atualizar(Long id, ParceiroEntrada entrada) {
        Parceiro parceiro = buscarPorId(id);
        preencher(parceiro, entrada);

        return ParceiroResponse.de(repositorio.save(parceiro));
    }

    @Transactional
    public ParceiroResponse ativar(Long id) {
        Parceiro parceiro = buscarPorId(id);

        if (Boolean.TRUE.equals(parceiro.getAtivo())) {
            throw new RegraDeNegocioException("O parceiro já está ativo.");
        }

        parceiro.setAtivo(true);
        return ParceiroResponse.de(repositorio.save(parceiro));
    }

    @Transactional
    public void desativar(Long id) {
        Parceiro parceiro = buscarPorId(id);

        if (!Boolean.TRUE.equals(parceiro.getAtivo())) {
            throw new ResourceNotFoundException(
                    "Parceiro não encontrado ou já desativado.");
        }

        parceiro.setAtivo(false);
        repositorio.save(parceiro);
    }

    private List<ParceiroResponse> converterLista(List<Parceiro> parceiros) {
        return parceiros.stream()
                .map(ParceiroResponse::de)
                .toList();
    }

    private Parceiro buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parceiro não encontrado."));
    }

    private void preencher(Parceiro parceiro, ParceiroEntrada entrada) {
        parceiro.setNome(entrada.nome().trim());
        parceiro.setDescricao(entrada.descricao().trim());
        parceiro.setLogoUrl(normalizarEValidarUrl(entrada.logoUrl(), "A URL do logo"));
        parceiro.setSite(normalizarEValidarUrl(entrada.site(), "O site"));
        parceiro.setEmail(normalizarEmail(entrada.email()));
        parceiro.setTelefone(normalizarOpcional(entrada.telefone()));
    }

    private String normalizarEValidarUrl(String valor, String nomeDoCampo) {
        String url = normalizarOpcional(valor);

        if (url == null) {
            return null;
        }

        try {
            URI uri = new URI(url);
            String esquema = uri.getScheme();

            if (uri.getHost() == null
                    || !("http".equalsIgnoreCase(esquema)
                    || "https".equalsIgnoreCase(esquema))) {
                throw new RegraDeNegocioException(
                        nomeDoCampo + " deve ser uma URL HTTP ou HTTPS válida.");
            }
        } catch (URISyntaxException excecao) {
            throw new RegraDeNegocioException(
                    nomeDoCampo + " deve ser uma URL HTTP ou HTTPS válida.");
        }

        return url;
    }

    private String normalizarEmail(String valor) {
        String email = normalizarOpcional(valor);
        return email == null ? null : email.toLowerCase(Locale.ROOT);
    }

    private String normalizarOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
