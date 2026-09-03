package com.GuapiVerde.mvp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.TemporadaEntrada;
import com.GuapiVerde.mvp.dto.TemporadaResponse;
import com.GuapiVerde.mvp.entity.Temporada;
import com.GuapiVerde.mvp.exception.RegraDeNegocioException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.TemporadaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemporadaService {

    private final TemporadaRepository repositorio;

    @Transactional(readOnly = true)
    public List<TemporadaResponse> listarPublicadas() {
        return repositorio.findAllByAtivoTrueOrderByDataInicioAsc().stream()
                .map(TemporadaResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public TemporadaResponse obterPublicadaPorId(Long id) {
        Temporada temporada = repositorio.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Temporada não encontrada ou desativada."));

        return TemporadaResponse.de(temporada);
    }

    @Transactional(readOnly = true)
    public List<TemporadaResponse> listarParaAdministracao() {
        return repositorio.findAllByOrderByDataInicioAsc().stream()
                .map(TemporadaResponse::de)
                .toList();
    }

    @Transactional
    public TemporadaResponse cadastrar(TemporadaEntrada entrada) {
        validarDatas(entrada);

        Temporada temporada = new Temporada();
        preencher(temporada, entrada);
        temporada.setAtivo(true);

        return TemporadaResponse.de(repositorio.save(temporada));
    }

    @Transactional
    public TemporadaResponse atualizar(Long id, TemporadaEntrada entrada) {
        Temporada temporada = buscarPorId(id);
        validarDatas(entrada);
        preencher(temporada, entrada);

        return TemporadaResponse.de(repositorio.save(temporada));
    }

    @Transactional
    public TemporadaResponse ativar(Long id) {
        Temporada temporada = buscarPorId(id);

        if (Boolean.TRUE.equals(temporada.getAtivo())) {
            throw new RegraDeNegocioException("A temporada já está ativa.");
        }

        temporada.setAtivo(true);
        return TemporadaResponse.de(repositorio.save(temporada));
    }

    @Transactional
    public void desativar(Long id) {
        Temporada temporada = buscarPorId(id);

        if (!Boolean.TRUE.equals(temporada.getAtivo())) {
            throw new ResourceNotFoundException(
                    "Temporada não encontrada ou já desativada.");
        }

        temporada.setAtivo(false);
        repositorio.save(temporada);
    }

    private Temporada buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Temporada não encontrada."));
    }

    private void validarDatas(TemporadaEntrada entrada) {
        if (entrada.dataFim().isBefore(entrada.dataInicio())) {
            throw new RegraDeNegocioException(
                    "A data de fim não pode ser anterior à data de início.");
        }
    }

    private void preencher(Temporada temporada, TemporadaEntrada entrada) {
        temporada.setNome(entrada.nome().trim());
        temporada.setDescricao(entrada.descricao().trim());
        temporada.setDataInicio(entrada.dataInicio());
        temporada.setDataFim(entrada.dataFim());
    }
}
