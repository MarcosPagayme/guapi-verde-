package com.GuapiVerde.mvp.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.GuapiVerde.mvp.dto.NovidadeEntrada;
import com.GuapiVerde.mvp.dto.NovidadeResponse;
import com.GuapiVerde.mvp.entity.Novidade;
import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.enums.PerfilUsuario;
import com.GuapiVerde.mvp.enums.SituacaoNovidade;
import com.GuapiVerde.mvp.exception.RegraDeNegocioException;
import com.GuapiVerde.mvp.exception.ResourceNotFoundException;
import com.GuapiVerde.mvp.repository.NovidadeRepository;
import com.GuapiVerde.mvp.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NovidadeService {

    private final NovidadeRepository repositorio;
    private final UsuarioRepository usuarioRepositorio;

    @Transactional(readOnly = true)
    public List<NovidadeResponse> listarPublicadas() {
        return converterLista(
                repositorio.findAllBySituacaoOrderByDataPublicacaoDesc(
                        SituacaoNovidade.PUBLICADA));
    }

    @Transactional(readOnly = true)
    public NovidadeResponse obterPublicadaPorId(Long id) {
        Novidade novidade = repositorio.findByIdAndSituacao(id, SituacaoNovidade.PUBLICADA)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Novidade não encontrada ou não publicada."));

        return NovidadeResponse.de(novidade);
    }

    @Transactional(readOnly = true)
    public List<NovidadeResponse> listarParaAdministracao() {
        return converterLista(repositorio.findAllByOrderByDataAtualizacaoDesc());
    }

    @Transactional(readOnly = true)
    public NovidadeResponse obterParaAdministracaoPorId(Long id) {
        return NovidadeResponse.de(buscarPorId(id));
    }

    @Transactional
    public NovidadeResponse cadastrar(String emailAdministrador, NovidadeEntrada entrada) {
        Usuario autor = buscarAdministradorAtivo(emailAdministrador);
        Novidade novidade = new Novidade();

        preencher(novidade, entrada);
        novidade.setSituacao(SituacaoNovidade.RASCUNHO);
        novidade.setDataPublicacao(null);
        novidade.setAutor(autor);

        return NovidadeResponse.de(repositorio.save(novidade));
    }

    @Transactional
    public NovidadeResponse atualizar(Long id, NovidadeEntrada entrada) {
        Novidade novidade = buscarPorId(id);
        preencher(novidade, entrada);

        return NovidadeResponse.de(repositorio.save(novidade));
    }

    @Transactional
    public NovidadeResponse publicar(Long id) {
        Novidade novidade = buscarPorId(id);

        if (novidade.getSituacao() == SituacaoNovidade.PUBLICADA) {
            throw new RegraDeNegocioException("A novidade já está publicada.");
        }

        novidade.setSituacao(SituacaoNovidade.PUBLICADA);
        novidade.setDataPublicacao(LocalDateTime.now());

        return NovidadeResponse.de(repositorio.save(novidade));
    }

    @Transactional
    public void arquivar(Long id) {
        Novidade novidade = buscarPorId(id);

        if (novidade.getSituacao() == SituacaoNovidade.ARQUIVADA) {
            throw new RegraDeNegocioException("A novidade já está arquivada.");
        }

        novidade.setSituacao(SituacaoNovidade.ARQUIVADA);
        repositorio.save(novidade);
    }

    private List<NovidadeResponse> converterLista(List<Novidade> novidades) {
        return novidades.stream()
                .map(NovidadeResponse::de)
                .toList();
    }

    private Novidade buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Novidade não encontrada."));
    }

    private Usuario buscarAdministradorAtivo(String email) {
        Usuario usuario = usuarioRepositorio.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário administrador não encontrado."));

        if (!Boolean.TRUE.equals(usuario.getAtivo())
                || usuario.getPerfil() != PerfilUsuario.ADMIN) {
            throw new RegraDeNegocioException(
                    "O autor deve ser um usuário administrador ativo.");
        }

        return usuario;
    }

    private void preencher(Novidade novidade, NovidadeEntrada entrada) {
        novidade.setTitulo(entrada.titulo().trim());
        novidade.setResumo(entrada.resumo().trim());
        novidade.setConteudo(entrada.conteudo().trim());
        novidade.setImagemUrl(normalizarEValidarImagemUrl(entrada.imagemUrl()));
    }

    private String normalizarEValidarImagemUrl(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String url = valor.trim();

        try {
            URI uri = new URI(url);
            String esquema = uri.getScheme();

            if (uri.getHost() == null
                    || !("http".equalsIgnoreCase(esquema)
                    || "https".equalsIgnoreCase(esquema))) {
                throw new RegraDeNegocioException(
                        "A URL da imagem deve ser uma URL HTTP ou HTTPS válida.");
            }
        } catch (URISyntaxException excecao) {
            throw new RegraDeNegocioException(
                    "A URL da imagem deve ser uma URL HTTP ou HTTPS válida.");
        }

        return url;
    }
}
