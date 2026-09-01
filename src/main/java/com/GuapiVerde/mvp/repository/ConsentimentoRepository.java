package com.GuapiVerde.mvp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GuapiVerde.mvp.entity.Consentimento;
import com.GuapiVerde.mvp.enums.TipoConsentimento;

public interface ConsentimentoRepository extends JpaRepository<Consentimento, Long> {

    List<Consentimento> findAllByUsuarioIdOrderByDataRegistroDescIdDesc(Long usuarioId);

    Optional<Consentimento> findFirstByUsuarioIdAndTipoOrderByDataRegistroDescIdDesc(
            Long usuarioId,
            TipoConsentimento tipo);
}
