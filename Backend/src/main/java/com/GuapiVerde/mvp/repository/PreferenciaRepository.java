package com.GuapiVerde.mvp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GuapiVerde.mvp.entity.Preferencia;

public interface PreferenciaRepository extends JpaRepository<Preferencia, Long> {

    boolean existsByUsuarioIdAndCategoriaAtrativoId(Long usuarioId, Long categoriaAtrativoId);

    Optional<Preferencia> findByUsuarioIdAndCategoriaAtrativoId(Long usuarioId, Long categoriaAtrativoId);

    List<Preferencia> findAllByUsuarioIdOrderByCategoriaAtrativoNomeAsc(Long usuarioId);
}
