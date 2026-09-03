package com.GuapiVerde.mvp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GuapiVerde.mvp.entity.Parceiro;

public interface ParceiroRepository extends JpaRepository<Parceiro, Long> {

    List<Parceiro> findAllByAtivoTrueOrderByNomeAsc();

    Optional<Parceiro> findByIdAndAtivoTrue(Long id);

    List<Parceiro> findAllByOrderByNomeAsc();
}
