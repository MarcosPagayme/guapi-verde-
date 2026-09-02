package com.GuapiVerde.mvp.entity;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.enums.SituacaoNovidade;

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
        name = "novidades",
        indexes = {
                @Index(name = "idx_novidades_data_publicacao", columnList = "data_publicacao"),
                @Index(name = "idx_novidades_situacao", columnList = "situacao")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Novidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String titulo;

    @Column(nullable = false, length = 300)
    private String resumo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoNovidade situacao;

    @Column(name = "data_publicacao")
    private LocalDateTime dataPublicacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @PrePersist
    public void antesDeCadastrar() {
        LocalDateTime agora = LocalDateTime.now();

        if (situacao == null) {
            situacao = SituacaoNovidade.RASCUNHO;
        }

        dataCadastro = agora;
        dataAtualizacao = agora;
    }

    @PreUpdate
    public void antesDeAtualizar() {
        dataAtualizacao = LocalDateTime.now();
    }
}
