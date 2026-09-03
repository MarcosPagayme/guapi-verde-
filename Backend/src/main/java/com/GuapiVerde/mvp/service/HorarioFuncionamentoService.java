package com.GuapiVerde.mvp.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.HorarioFuncionamentoEntrada;
import com.GuapiVerde.mvp.dto.HorarioFuncionamentoResponse;
import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.entity.HorarioFuncionamento;
import com.GuapiVerde.mvp.exception.RegraDeNegocioException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.AtrativoRepository;
import com.GuapiVerde.mvp.repository.HorarioFuncionamentoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HorarioFuncionamentoService {

    private final HorarioFuncionamentoRepository repositorio;
    private final AtrativoRepository atrativoRepositorio;

    @Transactional(readOnly = true)
    public List<HorarioFuncionamentoResponse> listar() {
        return repositorio.findAll().stream()
                .map(HorarioFuncionamentoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public HorarioFuncionamentoResponse obterPorId(Long id) {
        return HorarioFuncionamentoResponse.de(buscarHorario(id));
    }

    @Transactional(readOnly = true)
    public List<HorarioFuncionamentoResponse> listarPorAtrativo(Long atrativoId) {
        buscarAtrativoAtivo(atrativoId);

        return repositorio.findAllByAtrativoIdOrderByIdAsc(atrativoId).stream()
                .map(HorarioFuncionamentoResponse::de)
                .toList();
    }

    @Transactional
    public HorarioFuncionamentoResponse cadastrar(HorarioFuncionamentoEntrada entrada) {
        Atrativo atrativo = buscarAtrativoAtivo(entrada.atrativoId());
        validarHorarios(entrada);

        HorarioFuncionamento horario = new HorarioFuncionamento();
        preencher(horario, entrada, atrativo);

        return HorarioFuncionamentoResponse.de(repositorio.save(horario));
    }

    @Transactional
    public HorarioFuncionamentoResponse atualizar(Long id, HorarioFuncionamentoEntrada entrada) {
        HorarioFuncionamento horario = buscarHorario(id);
        Atrativo atrativo = buscarAtrativoAtivo(entrada.atrativoId());
        validarHorarios(entrada);

        preencher(horario, entrada, atrativo);

        return HorarioFuncionamentoResponse.de(repositorio.save(horario));
    }

    @Transactional
    public void excluir(Long id) {
        HorarioFuncionamento horario = buscarHorario(id);
        repositorio.delete(horario);
    }

    private HorarioFuncionamento buscarHorario(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Horário de funcionamento não encontrado."));
    }

    private Atrativo buscarAtrativoAtivo(Long atrativoId) {
        return atrativoRepositorio.findByIdAndAtivoTrue(atrativoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Atrativo não encontrado ou desativado."));
    }

    private void validarHorarios(HorarioFuncionamentoEntrada entrada) {
        if (Boolean.TRUE.equals(entrada.fechado())) {
            return;
        }

        LocalTime abertura = entrada.horarioAbertura();
        LocalTime fechamento = entrada.horarioFechamento();

        if (abertura == null || fechamento == null) {
            throw new RegraDeNegocioException(
                    "Os horários de abertura e fechamento são obrigatórios quando o local não está fechado.");
        }

        if (!abertura.isBefore(fechamento)) {
            throw new RegraDeNegocioException(
                    "O horário de abertura deve ser anterior ao horário de fechamento.");
        }
    }

    private void preencher(
            HorarioFuncionamento horario,
            HorarioFuncionamentoEntrada entrada,
            Atrativo atrativo) {
        horario.setAtrativo(atrativo);
        horario.setDiaSemana(entrada.diaSemana().trim());
        horario.setFechado(entrada.fechado());
        horario.setObservacao(normalizarOpcional(entrada.observacao()));

        if (Boolean.TRUE.equals(entrada.fechado())) {
            horario.setHorarioAbertura(null);
            horario.setHorarioFechamento(null);
        } else {
            horario.setHorarioAbertura(entrada.horarioAbertura());
            horario.setHorarioFechamento(entrada.horarioFechamento());
        }
    }

    private String normalizarOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }
}
