package com.GuapiVerde.mvp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "imagens_atrativos")
@Getter
@Setter
@NoArgsConstructor
public class ImagemAtrativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atrativo_id", nullable = false)
    private Atrativo atrativo;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(name = "texto_alternativo", nullable = false, length = 180)
    private String textoAlternativo;

    @Column(nullable = false)
    private Boolean principal;

    @Column(nullable = false)
    private Integer ordem;
}
