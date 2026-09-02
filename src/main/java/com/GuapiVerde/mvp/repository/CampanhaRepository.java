package com.GuapiVerde.mvp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.GuapiVerde.mvp.entity.Campanha;

public interface CampanhaRepository extends JpaRepository<Campanha, Long> {

    @Query("""
            SELECT campanha
            FROM Campanha campanha
            JOIN FETCH campanha.parceiro parceiro
            WHERE campanha.ativo = true
              AND parceiro.ativo = true
              AND campanha.dataInicio <= :dataAtual
              AND campanha.dataFim >= :dataAtual
            ORDER BY campanha.dataFim ASC, campanha.id ASC
            """)
    List<Campanha> listarPublicadas(@Param("dataAtual") LocalDate dataAtual);

    @Query("""
            SELECT campanha
            FROM Campanha campanha
            JOIN FETCH campanha.parceiro parceiro
            WHERE campanha.id = :id
              AND campanha.ativo = true
              AND parceiro.ativo = true
              AND campanha.dataInicio <= :dataAtual
              AND campanha.dataFim >= :dataAtual
            """)
    Optional<Campanha> buscarPublicadaPorId(
            @Param("id") Long id,
            @Param("dataAtual") LocalDate dataAtual);

    @Query("""
            SELECT campanha
            FROM Campanha campanha
            JOIN FETCH campanha.parceiro parceiro
            WHERE parceiro.id = :parceiroId
              AND campanha.ativo = true
              AND parceiro.ativo = true
              AND campanha.dataInicio <= :dataAtual
              AND campanha.dataFim >= :dataAtual
            ORDER BY campanha.dataFim ASC, campanha.id ASC
            """)
    List<Campanha> listarPublicadasPorParceiro(
            @Param("parceiroId") Long parceiroId,
            @Param("dataAtual") LocalDate dataAtual);

    @Query("""
            SELECT campanha
            FROM Campanha campanha
            JOIN FETCH campanha.parceiro
            ORDER BY campanha.dataInicio DESC
            """)
    List<Campanha> listarParaAdministracao();

    @Query("""
            SELECT campanha
            FROM Campanha campanha
            JOIN FETCH campanha.parceiro
            WHERE campanha.id = :id
            """)
    Optional<Campanha> buscarParaAdministracaoPorId(@Param("id") Long id);
}
