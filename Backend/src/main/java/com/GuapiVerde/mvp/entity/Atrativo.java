package com.GuapiVerde.mvp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.GuapiVerde.mvp.enums.SituacaoAtrativo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "atrativos",
        indexes = {
                @Index(name = "idx_atrativos_nome", columnList = "nome"),
                @Index(name = "idx_atrativos_categoria", columnList = "categoria_atrativo_id"),
                @Index(name = "idx_atrativos_ativo", columnList = "ativo")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Atrativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 300)
    private String resumo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(length = 255)
    private String endereco;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(length = 30)
    private String telefone;

    @Column(length = 255)
    private String site;

    @Column(nullable = false)
    private Boolean gratuito;

    @Column(name = "valor_entrada", precision = 10, scale = 2)
    private BigDecimal valorEntrada;

    @Column(nullable = false)
    private Boolean acessivel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private SituacaoAtrativo situacao;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_atrativo_id", nullable = false)
    private CategoriaAtrativo categoria;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @PrePersist
    public void antesDeCadastrar() {
        LocalDateTime agora = LocalDateTime.now();
        this.dataCadastro = agora;
        this.dataAtualizacao = agora;

        if (this.ativo == null) {
            this.ativo = true;
        }

        if (this.situacao == null) {
            this.situacao = SituacaoAtrativo.ABERTO;
        }
    }

    @PreUpdate
    public void antesDeAtualizar() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}
