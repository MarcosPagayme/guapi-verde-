package com.GuapiVerde.mvp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GuapiVerde.mvp.entity.ImagemAtrativo;

public interface ImagemAtrativoRepository extends JpaRepository<ImagemAtrativo, Long> {

    List<ImagemAtrativo> findAllByOrderByAtrativoNomeAscOrdemAscIdAsc();

    List<ImagemAtrativo> findAllByAtrativoIdOrderByOrdemAscIdAsc(Long atrativoId);

    List<ImagemAtrativo> findAllByAtrativoIdAndPrincipalTrue(Long atrativoId);
}
