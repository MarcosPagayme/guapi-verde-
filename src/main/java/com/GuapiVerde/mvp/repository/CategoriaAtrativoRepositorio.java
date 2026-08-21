package com.GuapiVerde.mvp.repository;

import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaAtrativoRepositorio
        extends JpaRepository<CategoriaAtrativo, Long> {

    boolean existsByNomeIgnoreCase(String nome);
}
