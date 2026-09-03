package com.GuapiVerde.mvp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GuapiVerde.mvp.entity.Atrativo;

public interface AtrativoRepository extends JpaRepository<Atrativo, Long> {

    List<Atrativo> findAllByAtivoTrueOrderByNomeAsc();

    Optional<Atrativo> findByIdAndAtivoTrue(Long id);

    List<Atrativo> findAllByCategoriaIdAndAtivoTrueOrderByNomeAsc(Long categoriaId);

    boolean existsByNomeIgnoreCaseAndAtivoTrue(String nome);

    boolean existsByNomeIgnoreCaseAndIdNotAndAtivoTrue(String nome, Long id);
}
