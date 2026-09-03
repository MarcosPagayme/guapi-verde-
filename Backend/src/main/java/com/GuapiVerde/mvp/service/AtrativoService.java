package com.GuapiVerde.mvp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.AtrativoEntrada;
import com.GuapiVerde.mvp.dto.AtrativoResponse;
import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import com.GuapiVerde.mvp.exception.RegraDeNegocioException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.AtrativoRepository;
import com.GuapiVerde.mvp.repository.CategoriaAtrativoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AtrativoService {

    private final AtrativoRepository repositorio;
    private final CategoriaAtrativoRepository categoriaRepositorio;

    @Transactional(readOnly = true)
    public List<AtrativoResponse> listar() {
        return repositorio.findAllByAtivoTrueOrderByNomeAsc().stream()
                .map(AtrativoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public AtrativoResponse obterPorId(Long id) {
        return AtrativoResponse.de(buscarAtrativoAtivo(id));
    }

    @Transactional(readOnly = true)
    public List<AtrativoResponse> listarPorCategoria(Long categoriaId) {
        buscarCategoriaAtiva(categoriaId);

        return repositorio.findAllByCategoriaIdAndAtivoTrueOrderByNomeAsc(categoriaId).stream()
                .map(AtrativoResponse::de)
                .toList();
    }

    @Transactional
    public AtrativoResponse cadastrar(AtrativoEntrada entrada) {
        validarCoordenadas(entrada);
        CategoriaAtrativo categoria = buscarCategoriaAtiva(entrada.categoriaAtrativoId());

        Atrativo atrativo = new Atrativo();
        preencher(atrativo, entrada, categoria);
        atrativo.setAtivo(true);

        return AtrativoResponse.de(repositorio.save(atrativo));
    }

    @Transactional
    public AtrativoResponse atualizar(Long id, AtrativoEntrada entrada) {
        Atrativo atrativo = buscarAtrativoAtivo(id);
        validarCoordenadas(entrada);
        CategoriaAtrativo categoria = buscarCategoriaAtiva(entrada.categoriaAtrativoId());

        preencher(atrativo, entrada, categoria);
        return AtrativoResponse.de(repositorio.save(atrativo));
    }

    @Transactional
    public void desativar(Long id) {
        Atrativo atrativo = buscarAtrativoAtivo(id);
        atrativo.setAtivo(false);
        repositorio.save(atrativo);
    }

    private Atrativo buscarAtrativoAtivo(Long id) {
        return repositorio.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Atrativo não encontrado ou desativado."));
    }

    private CategoriaAtrativo buscarCategoriaAtiva(Long categoriaId) {
        return categoriaRepositorio.findByIdAndAtivoTrue(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria de atrativo não encontrada ou desativada."));
    }

    private void validarCoordenadas(AtrativoEntrada entrada) {
        boolean possuiLatitude = entrada.latitude() != null;
        boolean possuiLongitude = entrada.longitude() != null;

        if (possuiLatitude != possuiLongitude) {
            throw new RegraDeNegocioException(
                    "Latitude e longitude devem ser informadas juntas.");
        }
    }

    private void preencher(
            Atrativo atrativo,
            AtrativoEntrada entrada,
            CategoriaAtrativo categoria) {
        atrativo.setNome(entrada.nome().trim());
        atrativo.setResumo(entrada.resumo().trim());
        atrativo.setDescricao(entrada.descricao().trim());
        atrativo.setEndereco(normalizarOpcional(entrada.endereco()));
        atrativo.setLatitude(entrada.latitude());
        atrativo.setLongitude(entrada.longitude());
        atrativo.setTelefone(normalizarOpcional(entrada.telefone()));
        atrativo.setSite(normalizarOpcional(entrada.site()));
        atrativo.setGratuito(entrada.gratuito());
        atrativo.setValorEntrada(Boolean.TRUE.equals(entrada.gratuito())
                ? null
                : entrada.valorEntrada());
        atrativo.setAcessivel(entrada.acessivel());
        atrativo.setSituacao(entrada.situacao());
        atrativo.setCategoria(categoria);
    }

    private String normalizarOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }
}
