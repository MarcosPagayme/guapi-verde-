package com.GuapiVerde.mvp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "eventos",
        indexes = {
                @Index(name = "idx_eventos_data_hora_inicio", columnList = "data_hora_inicio"),
                @Index(name = "idx_eventos_ativo", columnList = "ativo")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 300)
    private String resumo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim")
    private LocalDateTime dataHoraFim;

    @Column(length = 255)
    private String local;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atrativo_id")
    private Atrativo atrativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "temporada_id")
    private Temporada temporada;
}
