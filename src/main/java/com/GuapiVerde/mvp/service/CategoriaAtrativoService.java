package com.GuapiVerde.mvp.service;

import com.GuapiVerde.mvp.dto.CategoriaAtrativoEntrada;
import com.GuapiVerde.mvp.dto.CategoriaAtrativoResposta;
import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import com.GuapiVerde.mvp.exception.DuplicateResourceException;
import com.GuapiVerde.mvp.repository.CategoriaAtrativoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaAtrativoService {

    private final CategoriaAtrativoRepositorio repositorio;

    @Transactional
    public CategoriaAtrativoResposta cadastrar(
            CategoriaAtrativoEntrada entrada
    ) {
        String nome = entrada.nome().trim();

        if (repositorio.existsByNomeIgnoreCase(nome)) {
            throw new DuplicateResourceException(
                    "Já existe uma categoria cadastrada com esse nome."
            );
        }

        CategoriaAtrativo categoria = new CategoriaAtrativo();
        categoria.setNome(nome);
        categoria.setDescricao(
                entrada.descricao() == null
                        ? null
                        : entrada.descricao().trim()
        );
        categoria.setAtivo(true);

        CategoriaAtrativo categoriaSalva = repositorio.save(categoria);

        return CategoriaAtrativoResposta.de(categoriaSalva);
    }
}
