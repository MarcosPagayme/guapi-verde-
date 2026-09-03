package com.GuapiVerde.mvp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.GuapiVerde.mvp.entity.Cupom;

public interface CupomRepository extends JpaRepository<Cupom, Long> {

    boolean existsByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);

    @Query("""
            SELECT cupom
            FROM Cupom cupom
            JOIN FETCH cupom.campanha campanha
            JOIN FETCH campanha.parceiro parceiro
            WHERE cupom.ativo = true
              AND cupom.dataValidade >= :dataAtual
              AND (cupom.quantidadeDisponivel IS NULL OR cupom.quantidadeDisponivel > 0)
              AND campanha.ativo = true
              AND campanha.dataInicio <= :dataAtual
              AND campanha.dataFim >= :dataAtual
              AND parceiro.ativo = true
            ORDER BY cupom.dataValidade ASC, cupom.id ASC
            """)
    List<Cupom> listarDisponiveis(@Param("dataAtual") LocalDate dataAtual);

    @Query("""
            SELECT cupom
            FROM Cupom cupom
            JOIN FETCH cupom.campanha campanha
            JOIN FETCH campanha.parceiro parceiro
            WHERE cupom.id = :id
              AND cupom.ativo = true
              AND cupom.dataValidade >= :dataAtual
              AND (cupom.quantidadeDisponivel IS NULL OR cupom.quantidadeDisponivel > 0)
              AND campanha.ativo = true
              AND campanha.dataInicio <= :dataAtual
              AND campanha.dataFim >= :dataAtual
              AND parceiro.ativo = true
            """)
    Optional<Cupom> buscarDisponivelPorId(
            @Param("id") Long id,
            @Param("dataAtual") LocalDate dataAtual);

    @Query("""
            SELECT cupom
            FROM Cupom cupom
            JOIN FETCH cupom.campanha campanha
            JOIN FETCH campanha.parceiro parceiro
            WHERE UPPER(cupom.codigo) = UPPER(:codigo)
              AND cupom.ativo = true
              AND cupom.dataValidade >= :dataAtual
              AND (cupom.quantidadeDisponivel IS NULL OR cupom.quantidadeDisponivel > 0)
              AND campanha.ativo = true
              AND campanha.dataInicio <= :dataAtual
              AND campanha.dataFim >= :dataAtual
              AND parceiro.ativo = true
            """)
    Optional<Cupom> buscarDisponivelPorCodigo(
            @Param("codigo") String codigo,
            @Param("dataAtual") LocalDate dataAtual);

    @Query("""
            SELECT cupom
            FROM Cupom cupom
            JOIN FETCH cupom.campanha campanha
            JOIN FETCH campanha.parceiro parceiro
            WHERE campanha.id = :campanhaId
              AND cupom.ativo = true
              AND cupom.dataValidade >= :dataAtual
              AND (cupom.quantidadeDisponivel IS NULL OR cupom.quantidadeDisponivel > 0)
              AND campanha.ativo = true
              AND campanha.dataInicio <= :dataAtual
              AND campanha.dataFim >= :dataAtual
              AND parceiro.ativo = true
            ORDER BY cupom.dataValidade ASC, cupom.id ASC
            """)
    List<Cupom> listarDisponiveisPorCampanha(
            @Param("campanhaId") Long campanhaId,
            @Param("dataAtual") LocalDate dataAtual);

    @Query("""
            SELECT cupom
            FROM Cupom cupom
            JOIN FETCH cupom.campanha campanha
            JOIN FETCH campanha.parceiro
            ORDER BY cupom.id DESC
            """)
    List<Cupom> listarParaAdministracao();

    @Query("""
            SELECT cupom
            FROM Cupom cupom
            JOIN FETCH cupom.campanha campanha
            JOIN FETCH campanha.parceiro
            WHERE cupom.id = :id
            """)
    Optional<Cupom> buscarParaAdministracaoPorId(@Param("id") Long id);
}
