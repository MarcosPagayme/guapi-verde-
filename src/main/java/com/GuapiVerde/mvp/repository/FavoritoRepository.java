package com.GuapiVerde.mvp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GuapiVerde.mvp.entity.Favorito;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    boolean existsByUsuarioIdAndAtrativoId(Long usuarioId, Long atrativoId);

    Optional<Favorito> findByUsuarioIdAndAtrativoId(Long usuarioId, Long atrativoId);

    List<Favorito> findAllByUsuarioIdOrderByDataCadastroDesc(Long usuarioId);
}
