package com.GuapiVerde.mvp.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.CampanhaEntrada;
import com.GuapiVerde.mvp.dto.CampanhaResponse;
import com.GuapiVerde.mvp.entity.Campanha;
import com.GuapiVerde.mvp.entity.Parceiro;
import com.GuapiVerde.mvp.exception.RegraDeNegocioException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.CampanhaRepository;
import com.GuapiVerde.mvp.repository.ParceiroRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampanhaService {

    private final CampanhaRepository repositorio;
    private final ParceiroRepository parceiroRepositorio;

    @Transactional(readOnly = true)
    public List<CampanhaResponse> listarPublicadas() {
        return converterLista(repositorio.listarPublicadas(LocalDate.now()));
    }

    @Transactional(readOnly = true)
    public CampanhaResponse obterPublicadaPorId(Long id) {
        Campanha campanha = repositorio.buscarPublicadaPorId(id, LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Campanha não encontrada ou não disponível."));

        return CampanhaResponse.de(campanha);
    }

    @Transactional(readOnly = true)
    public List<CampanhaResponse> listarPublicadasPorParceiro(Long parceiroId) {
        return converterLista(
                repositorio.listarPublicadasPorParceiro(parceiroId, LocalDate.now()));
    }

    @Transactional(readOnly = true)
    public List<CampanhaResponse> listarParaAdministracao() {
        return converterLista(repositorio.listarParaAdministracao());
    }

    @Transactional(readOnly = true)
    public CampanhaResponse obterParaAdministracaoPorId(Long id) {
        return CampanhaResponse.de(buscarPorId(id));
    }

    @Transactional
    public CampanhaResponse cadastrar(CampanhaEntrada entrada) {
        validarDatas(entrada.dataInicio(), entrada.dataFim());

        Campanha campanha = new Campanha();
        preencher(campanha, entrada, buscarParceiroAtivo(entrada.parceiroId()));
        campanha.setAtivo(true);

        return CampanhaResponse.de(repositorio.save(campanha));
    }

    @Transactional
    public CampanhaResponse atualizar(Long id, CampanhaEntrada entrada) {
        Campanha campanha = buscarPorId(id);
        validarDatas(entrada.dataInicio(), entrada.dataFim());

        Parceiro parceiro = campanha.getParceiro().getId().equals(entrada.parceiroId())
                ? campanha.getParceiro()
                : buscarParceiroAtivo(entrada.parceiroId());

        preencher(campanha, entrada, parceiro);

        return CampanhaResponse.de(repositorio.save(campanha));
    }

    @Transactional
    public CampanhaResponse ativar(Long id) {
        Campanha campanha = buscarPorId(id);

        if (Boolean.TRUE.equals(campanha.getAtivo())) {
            throw new RegraDeNegocioException("A campanha já está ativa.");
        }

        campanha.setParceiro(buscarParceiroAtivo(campanha.getParceiro().getId()));
        campanha.setAtivo(true);

        return CampanhaResponse.de(repositorio.save(campanha));
    }

    @Transactional
    public void desativar(Long id) {
        Campanha campanha = buscarPorId(id);

        if (!Boolean.TRUE.equals(campanha.getAtivo())) {
            throw new ResourceNotFoundException(
                    "Campanha não encontrada ou já desativada.");
        }

        campanha.setAtivo(false);
        repositorio.save(campanha);
    }

    private List<CampanhaResponse> converterLista(List<Campanha> campanhas) {
        return campanhas.stream()
                .map(CampanhaResponse::de)
                .toList();
    }

    private Campanha buscarPorId(Long id) {
        return repositorio.buscarParaAdministracaoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Campanha não encontrada."));
    }

    private Parceiro buscarParceiroAtivo(Long parceiroId) {
        return parceiroRepositorio.findByIdAndAtivoTrue(parceiroId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parceiro não encontrado ou desativado."));
    }

    private void validarDatas(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            throw new RegraDeNegocioException(
                    "A data de fim não pode ser anterior à data de início.");
        }
    }

    private void preencher(
            Campanha campanha,
            CampanhaEntrada entrada,
            Parceiro parceiro) {
        campanha.setParceiro(parceiro);
        campanha.setTitulo(entrada.titulo().trim());
        campanha.setDescricao(entrada.descricao().trim());
        campanha.setDataInicio(entrada.dataInicio());
        campanha.setDataFim(entrada.dataFim());
        campanha.setImagemUrl(normalizarEValidarImagemUrl(entrada.imagemUrl()));
    }

    private String normalizarEValidarImagemUrl(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String url = valor.trim();

        try {
            URI uri = new URI(url);
            String esquema = uri.getScheme();

            if (uri.getHost() == null
                    || !("http".equalsIgnoreCase(esquema)
                    || "https".equalsIgnoreCase(esquema))) {
                throw new RegraDeNegocioException(
                        "A URL da imagem deve ser uma URL HTTP ou HTTPS válida.");
            }
        } catch (URISyntaxException excecao) {
            throw new RegraDeNegocioException(
                    "A URL da imagem deve ser uma URL HTTP ou HTTPS válida.");
        }

        return url;
    }
}
