package com.GuapiVerde.mvp.repository;

import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaAtrativoRepository
        extends JpaRepository<CategoriaAtrativo, Long> {
    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    Optional<CategoriaAtrativo> findByIdAndAtivoTrue(Long id);
}
