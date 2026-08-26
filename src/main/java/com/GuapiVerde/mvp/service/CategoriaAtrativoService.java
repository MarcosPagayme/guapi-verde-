package com.GuapiVerde.mvp.service;

import com.GuapiVerde.mvp.dto.CategoriaAtrativoEntrada;
import com.GuapiVerde.mvp.dto.CategoriaAtrativoResponse;
import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import com.GuapiVerde.mvp.exception.DuplicateResourceException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.CategoriaAtrativoRepositorio;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaAtrativoService {

    private final CategoriaAtrativoRepositorio repositorio;

    @Transactional
    public CategoriaAtrativoResponse cadastrar(
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

        return CategoriaAtrativoResponse.de(categoriaSalva);
    }

    @Transactional(readOnly = true)
    public List<CategoriaAtrativoResponse> listar() {
        return repositorio.findAll()
                .stream()
                .map(CategoriaAtrativoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaAtrativoResponse obterPorId(Long id) {
        CategoriaAtrativo categoria = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada."
                ));

        return CategoriaAtrativoResponse.de(categoria);
    }

    @Transactional
    public CategoriaAtrativoResponse atualizar(
            Long id,
            CategoriaAtrativoEntrada entrada
    ) {
        CategoriaAtrativo categoria = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada."
                ));

        String novoNome = entrada.nome().trim();

        if (repositorio.existsByNomeIgnoreCaseAndIdNot(novoNome, id)) {
            throw new DuplicateResourceException(
                    "Já existe uma categoria cadastrada com esse nome."
            );
        }

        categoria.setNome(novoNome);
        categoria.setDescricao(
                entrada.descricao() == null
                        ? null
                        : entrada.descricao().trim()
        );

        CategoriaAtrativo categoriaAtualizada = repositorio.save(categoria);

        return CategoriaAtrativoResponse.de(categoriaAtualizada);
    }

    @Transactional
    public void desativar(Long id) {
        CategoriaAtrativo categoria = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada."
                ));

        categoria.setAtivo(false);
        repositorio.save(categoria);
    }
}
