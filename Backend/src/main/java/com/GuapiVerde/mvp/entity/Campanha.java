package com.GuapiVerde.mvp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "campanhas",
        indexes = {
                @Index(name = "idx_campanhas_data_inicio_data_fim", columnList = "data_inicio, data_fim"),
                @Index(name = "idx_campanhas_ativo", columnList = "ativo")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Campanha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parceiro_id", nullable = false)
    private Parceiro parceiro;

    @Column(nullable = false, length = 160)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;

    @Column(nullable = false)
    private Boolean ativo = true;

    @PrePersist
    public void antesDeCadastrar() {
        if (ativo == null) {
            ativo = true;
        }
    }
}
