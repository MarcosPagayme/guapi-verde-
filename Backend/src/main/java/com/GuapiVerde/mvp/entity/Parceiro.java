package com.GuapiVerde.mvp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parceiros")
@Getter
@Setter
@NoArgsConstructor
public class Parceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(length = 255)
    private String site;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String telefone;

    @Column(nullable = false)
    private Boolean ativo = true;

    @PrePersist
    public void antesDeCadastrar() {
        if (ativo == null) {
            ativo = true;
        }
    }
}
