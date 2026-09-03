package com.GuapiVerde.mvp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GuapiVerde.mvp.entity.Temporada;

public interface TemporadaRepository extends JpaRepository<Temporada, Long> {

    List<Temporada> findAllByAtivoTrueOrderByDataInicioAsc();

    Optional<Temporada> findByIdAndAtivoTrue(Long id);

    List<Temporada> findAllByOrderByDataInicioAsc();
}
