package com.GuapiVerde.mvp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GuapiVerde.mvp.entity.Novidade;
import com.GuapiVerde.mvp.enums.SituacaoNovidade;

public interface NovidadeRepository extends JpaRepository<Novidade, Long> {

    List<Novidade> findAllBySituacaoOrderByDataPublicacaoDesc(SituacaoNovidade situacao);

    Optional<Novidade> findByIdAndSituacao(Long id, SituacaoNovidade situacao);

    List<Novidade> findAllByOrderByDataAtualizacaoDesc();

    List<Novidade> findAllBySituacaoOrderByDataAtualizacaoDesc(SituacaoNovidade situacao);
}
