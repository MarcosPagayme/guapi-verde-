package com.GuapiVerde.mvp.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GuapiVerde.mvp.entity.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findAllByAtivoTrueOrderByDataHoraInicioAsc();

    Optional<Evento> findByIdAndAtivoTrue(Long id);

    List<Evento> findAllByOrderByDataHoraInicioAsc();

    List<Evento> findAllByAtrativoIdAndAtivoTrueOrderByDataHoraInicioAsc(Long atrativoId);

    List<Evento> findAllByTemporadaIdAndAtivoTrueOrderByDataHoraInicioAsc(Long temporadaId);

    List<Evento> findAllByAtivoTrueAndDataHoraInicioBetweenOrderByDataHoraInicioAsc(
            LocalDateTime inicio,
            LocalDateTime fim);
}
