package com.GuapiVerde.mvp.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.EventoEntrada;
import com.GuapiVerde.mvp.dto.EventoResponse;
import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.entity.Evento;
import com.GuapiVerde.mvp.entity.Temporada;
import com.GuapiVerde.mvp.exception.RegraDeNegocioException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.AtrativoRepository;
import com.GuapiVerde.mvp.repository.EventoRepository;
import com.GuapiVerde.mvp.repository.TemporadaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository repositorio;
    private final AtrativoRepository atrativoRepositorio;
    private final TemporadaRepository temporadaRepositorio;

    @Transactional(readOnly = true)
    public List<EventoResponse> listarPublicados() {
        return converterLista(repositorio.findAllByAtivoTrueOrderByDataHoraInicioAsc());
    }

    @Transactional(readOnly = true)
    public EventoResponse obterPublicadoPorId(Long id) {
        Evento evento = repositorio.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Evento não encontrado ou desativado."));

        return EventoResponse.de(evento);
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listarParaAdministracao() {
        return converterLista(repositorio.findAllByOrderByDataHoraInicioAsc());
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listarPorAtrativo(Long atrativoId) {
        buscarAtrativoAtivo(atrativoId);
        return converterLista(
                repositorio.findAllByAtrativoIdAndAtivoTrueOrderByDataHoraInicioAsc(atrativoId));
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listarPorTemporada(Long temporadaId) {
        buscarTemporadaAtiva(temporadaId);
        return converterLista(
                repositorio.findAllByTemporadaIdAndAtivoTrueOrderByDataHoraInicioAsc(temporadaId));
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        validarPeriodo(inicio, fim);
        return converterLista(
                repositorio.findAllByAtivoTrueAndDataHoraInicioBetweenOrderByDataHoraInicioAsc(
                        inicio,
                        fim));
    }

    @Transactional
    public EventoResponse cadastrar(EventoEntrada entrada) {
        Evento evento = new Evento();
        preencher(evento, entrada);
        evento.setAtivo(true);

        return EventoResponse.de(repositorio.save(evento));
    }

    @Transactional
    public EventoResponse atualizar(Long id, EventoEntrada entrada) {
        Evento evento = buscarPorId(id);
        preencher(evento, entrada);

        return EventoResponse.de(repositorio.save(evento));
    }

    @Transactional
    public EventoResponse ativar(Long id) {
        Evento evento = buscarPorId(id);

        if (Boolean.TRUE.equals(evento.getAtivo())) {
            throw new RegraDeNegocioException("O evento já está ativo.");
        }

        validarRelacionamentosAtivos(evento);
        evento.setAtivo(true);

        return EventoResponse.de(repositorio.save(evento));
    }

    @Transactional
    public void desativar(Long id) {
        Evento evento = buscarPorId(id);

        if (!Boolean.TRUE.equals(evento.getAtivo())) {
            throw new ResourceNotFoundException(
                    "Evento não encontrado ou já desativado.");
        }

        evento.setAtivo(false);
        repositorio.save(evento);
    }

    private List<EventoResponse> converterLista(List<Evento> eventos) {
        return eventos.stream()
                .map(EventoResponse::de)
                .toList();
    }

    private Evento buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Evento não encontrado."));
    }

    private Atrativo buscarAtrativoAtivo(Long atrativoId) {
        return atrativoRepositorio.findByIdAndAtivoTrue(atrativoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Atrativo não encontrado ou desativado."));
    }

    private Temporada buscarTemporadaAtiva(Long temporadaId) {
        return temporadaRepositorio.findByIdAndAtivoTrue(temporadaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Temporada não encontrada ou desativada."));
    }

    private void validarPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new RegraDeNegocioException(
                    "As datas de início e fim do período são obrigatórias.");
        }

        if (fim.isBefore(inicio)) {
            throw new RegraDeNegocioException(
                    "O fim do período não pode ser anterior ao início.");
        }
    }

    private void validarDatas(LocalDateTime inicio, LocalDateTime fim) {
        if (fim != null && fim.isBefore(inicio)) {
            throw new RegraDeNegocioException(
                    "A data e hora de fim não podem ser anteriores à data e hora de início.");
        }
    }

    private void validarRelacionamentosAtivos(Evento evento) {
        if (evento.getAtrativo() != null) {
            evento.setAtrativo(buscarAtrativoAtivo(evento.getAtrativo().getId()));
        }

        if (evento.getTemporada() != null) {
            evento.setTemporada(buscarTemporadaAtiva(evento.getTemporada().getId()));
        }
    }

    private String normalizarTextoOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }

    private String normalizarEValidarImagemUrl(String valor) {
        String url = normalizarTextoOpcional(valor);

        if (url == null) {
            return null;
        }

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

    private void preencher(Evento evento, EventoEntrada entrada) {
        Atrativo atrativo = entrada.atrativoId() == null
                ? null
                : buscarAtrativoAtivo(entrada.atrativoId());
        Temporada temporada = entrada.temporadaId() == null
                ? null
                : buscarTemporadaAtiva(entrada.temporadaId());
        String local = normalizarTextoOpcional(entrada.local());

        validarDatas(entrada.dataHoraInicio(), entrada.dataHoraFim());

        if (atrativo == null && local == null) {
            throw new RegraDeNegocioException(
                    "O evento deve possuir um atrativo ou um local informado.");
        }

        evento.setNome(entrada.nome().trim());
        evento.setResumo(entrada.resumo().trim());
        evento.setDescricao(entrada.descricao().trim());
        evento.setDataHoraInicio(entrada.dataHoraInicio());
        evento.setDataHoraFim(entrada.dataHoraFim());
        evento.setLocal(local);
        evento.setImagemUrl(normalizarEValidarImagemUrl(entrada.imagemUrl()));
        evento.setAtrativo(atrativo);
        evento.setTemporada(temporada);
    }
}
