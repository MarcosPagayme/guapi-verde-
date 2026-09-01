package com.GuapiVerde.mvp.entity;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.enums.TipoConsentimento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consentimentos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Consentimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoConsentimento tipo;

    @Column(name = "versao_termo", nullable = false, length = 30)
    private String versaoTermo;

    @Column(nullable = false)
    private Boolean consentido;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;

    public Consentimento(
            Usuario usuario,
            TipoConsentimento tipo,
            String versaoTermo,
            Boolean consentido) {
        this.usuario = usuario;
        this.tipo = tipo;
        this.versaoTermo = versaoTermo;
        this.consentido = consentido;
    }

    @PrePersist
    public void antesDeRegistrar() {
        this.dataRegistro = LocalDateTime.now();
    }
}
