package com.GuapiVerde.mvp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.CupomEntrada;
import com.GuapiVerde.mvp.dto.CupomResponse;
import com.GuapiVerde.mvp.entity.Campanha;
import com.GuapiVerde.mvp.entity.Cupom;
import com.GuapiVerde.mvp.exception.DuplicateResourceException;
import com.GuapiVerde.mvp.exception.RegraDeNegocioException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.CampanhaRepository;
import com.GuapiVerde.mvp.repository.CupomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CupomService {

    private static final Pattern PADRAO_CODIGO = Pattern.compile("[\\p{L}\\p{N}_-]+");

    private final CupomRepository repositorio;
    private final CampanhaRepository campanhaRepositorio;

    @Transactional(readOnly = true)
    public List<CupomResponse> listarDisponiveis() {
        return converterLista(repositorio.listarDisponiveis(LocalDate.now()));
    }

    @Transactional(readOnly = true)
    public CupomResponse obterDisponivelPorId(Long id) {
        Cupom cupom = repositorio.buscarDisponivelPorId(id, LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cupom não encontrado ou não disponível."));

        return CupomResponse.de(cupom);
    }

    @Transactional(readOnly = true)
    public CupomResponse obterDisponivelPorCodigo(String codigo) {
        String codigoNormalizado = normalizarCodigo(codigo);
        Cupom cupom = repositorio.buscarDisponivelPorCodigo(
                        codigoNormalizado,
                        LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cupom não encontrado ou não disponível."));

        return CupomResponse.de(cupom);
    }

    @Transactional(readOnly = true)
    public List<CupomResponse> listarDisponiveisPorCampanha(Long campanhaId) {
        return converterLista(repositorio.listarDisponiveisPorCampanha(
                campanhaId,
                LocalDate.now()));
    }

    @Transactional(readOnly = true)
    public List<CupomResponse> listarParaAdministracao() {
        return converterLista(repositorio.listarParaAdministracao());
    }

    @Transactional(readOnly = true)
    public CupomResponse obterParaAdministracaoPorId(Long id) {
        return CupomResponse.de(buscarPorId(id));
    }

    @Transactional
    public CupomResponse cadastrar(CupomEntrada entrada) {
        Campanha campanha = buscarCampanhaAtiva(entrada.campanhaId());
        String codigo = normalizarCodigo(entrada.codigo());

        validarDuplicidadeNoCadastro(codigo);
        validarDados(entrada.dataValidade(), entrada.quantidadeDisponivel(), campanha);

        Cupom cupom = new Cupom();
        preencher(cupom, entrada, campanha, codigo);
        cupom.setAtivo(true);

        return CupomResponse.de(repositorio.save(cupom));
    }

    @Transactional
    public CupomResponse atualizar(Long id, CupomEntrada entrada) {
        Cupom cupom = buscarPorId(id);
        Campanha campanha = buscarCampanhaAtiva(entrada.campanhaId());
        String codigo = normalizarCodigo(entrada.codigo());

        validarDuplicidadeNaAtualizacao(codigo, id);
        validarDados(entrada.dataValidade(), entrada.quantidadeDisponivel(), campanha);
        preencher(cupom, entrada, campanha, codigo);

        return CupomResponse.de(repositorio.save(cupom));
    }

    @Transactional
    public CupomResponse ativar(Long id) {
        Cupom cupom = buscarPorId(id);

        if (Boolean.TRUE.equals(cupom.getAtivo())) {
            throw new RegraDeNegocioException("O cupom já está ativo.");
        }

        Campanha campanha = buscarCampanhaAtiva(cupom.getCampanha().getId());
        if (!Boolean.TRUE.equals(campanha.getParceiro().getAtivo())) {
            throw new ResourceNotFoundException(
                    "Parceiro da campanha não encontrado ou desativado.");
        }

        if (cupom.getDataValidade().isBefore(LocalDate.now())) {
            throw new RegraDeNegocioException("Não é possível ativar um cupom vencido.");
        }

        cupom.setCampanha(campanha);
        cupom.setAtivo(true);

        return CupomResponse.de(repositorio.save(cupom));
    }

    @Transactional
    public void desativar(Long id) {
        Cupom cupom = buscarPorId(id);

        if (!Boolean.TRUE.equals(cupom.getAtivo())) {
            throw new ResourceNotFoundException(
                    "Cupom não encontrado ou já desativado.");
        }

        cupom.setAtivo(false);
        repositorio.save(cupom);
    }

    private List<CupomResponse> converterLista(List<Cupom> cupons) {
        return cupons.stream()
                .map(CupomResponse::de)
                .toList();
    }

    private Cupom buscarPorId(Long id) {
        return repositorio.buscarParaAdministracaoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cupom não encontrado."));
    }

    private Campanha buscarCampanhaAtiva(Long campanhaId) {
        return campanhaRepositorio.buscarAtivaPorId(campanhaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Campanha não encontrada ou desativada."));
    }

    private void validarDuplicidadeNoCadastro(String codigo) {
        if (repositorio.existsByCodigoIgnoreCase(codigo)) {
            throw new DuplicateResourceException("Já existe um cupom com este código.");
        }
    }

    private void validarDuplicidadeNaAtualizacao(String codigo, Long id) {
        if (repositorio.existsByCodigoIgnoreCaseAndIdNot(codigo, id)) {
            throw new DuplicateResourceException("Já existe um cupom com este código.");
        }
    }

    private void validarDados(
            LocalDate dataValidade,
            Integer quantidadeDisponivel,
            Campanha campanha) {
        if (dataValidade == null) {
            throw new RegraDeNegocioException("A data de validade é obrigatória.");
        }

        if (dataValidade.isAfter(campanha.getDataFim())) {
            throw new RegraDeNegocioException(
                    "A data de validade do cupom não pode ser posterior à data de fim da campanha.");
        }

        if (quantidadeDisponivel != null && quantidadeDisponivel < 0) {
            throw new RegraDeNegocioException(
                    "A quantidade disponível deve ser maior ou igual a zero.");
        }
    }

    private String normalizarCodigo(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new RegraDeNegocioException("O código é obrigatório.");
        }

        String codigo = valor.trim().toUpperCase(Locale.ROOT);
        if (codigo.length() > 60) {
            throw new RegraDeNegocioException(
                    "O código deve possuir no máximo 60 caracteres.");
        }

        if (!PADRAO_CODIGO.matcher(codigo).matches()) {
            throw new RegraDeNegocioException(
                    "O código deve conter somente letras, números, hífen ou underscore, sem espaços internos.");
        }

        return codigo;
    }

    private void preencher(
            Cupom cupom,
            CupomEntrada entrada,
            Campanha campanha,
            String codigo) {
        cupom.setCampanha(campanha);
        cupom.setCodigo(codigo);
        cupom.setDescricao(entrada.descricao().trim());
        cupom.setRegrasUso(normalizarTextoOpcional(entrada.regrasUso()));
        cupom.setDataValidade(entrada.dataValidade());
        cupom.setQuantidadeDisponivel(entrada.quantidadeDisponivel());
    }

    private String normalizarTextoOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }
}
