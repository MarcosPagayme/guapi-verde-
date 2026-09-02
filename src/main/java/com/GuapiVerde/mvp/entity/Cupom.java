package com.GuapiVerde.mvp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "cupons",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_cupons_codigo",
                columnNames = "codigo")
)
@Getter
@Setter
@NoArgsConstructor
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campanha_id", nullable = false)
    private Campanha campanha;

    @Column(nullable = false, length = 60)
    private String codigo;

    @Column(nullable = false, length = 300)
    private String descricao;

    @Column(name = "regras_uso", columnDefinition = "TEXT")
    private String regrasUso;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Column(name = "quantidade_disponivel")
    private Integer quantidadeDisponivel;

    @Column(nullable = false)
    private Boolean ativo = true;

    @PrePersist
    public void antesDeCadastrar() {
        if (ativo == null) {
            ativo = true;
        }
    }
}
