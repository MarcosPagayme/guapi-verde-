package com.GuapiVerde.mvp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GuapiVerde.mvp.entity.HorarioFuncionamento;

public interface HorarioFuncionamentoRepository extends JpaRepository<HorarioFuncionamento, Long> {

    List<HorarioFuncionamento> findAllByAtrativoIdOrderByIdAsc(Long atrativoId);
}
