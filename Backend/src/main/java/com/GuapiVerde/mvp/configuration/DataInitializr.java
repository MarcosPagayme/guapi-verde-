package com.GuapiVerde.mvp.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.entity.Campanha;
import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import com.GuapiVerde.mvp.entity.Consentimento;
import com.GuapiVerde.mvp.entity.Cupom;
import com.GuapiVerde.mvp.entity.Evento;
import com.GuapiVerde.mvp.entity.Favorito;
import com.GuapiVerde.mvp.entity.HorarioFuncionamento;
import com.GuapiVerde.mvp.entity.ImagemAtrativo;
import com.GuapiVerde.mvp.entity.Novidade;
import com.GuapiVerde.mvp.entity.Parceiro;
import com.GuapiVerde.mvp.entity.Preferencia;
import com.GuapiVerde.mvp.entity.Temporada;
import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.enums.PerfilUsuario;
import com.GuapiVerde.mvp.enums.SituacaoAtrativo;
import com.GuapiVerde.mvp.enums.SituacaoNovidade;
import com.GuapiVerde.mvp.enums.TipoConsentimento;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Profile("!prod")
@ConditionalOnProperty(name = "configuracao.dados-iniciais.ativo", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class DataInitializr implements ApplicationRunner {

    private final EntityManager entityManager;
    private final PasswordEncoder codificadorDeSenha;

    private static final String FOTO_CAPELA = "https://thumb.wikimedia.org/wikipedia/commons/thumb/1/1f/"
            + "Capela_de_Nossa_Senhora_da_Concei%C3%A7%C3%A3o_HDR.jpg/"
            + "1280px-Capela_de_Nossa_Senhora_da_Concei%C3%A7%C3%A3o_HDR.jpg";
    private static final String FOTO_SEDE = "https://thumb.wikimedia.org/wikipedia/commons/thumb/7/7a/"
            + "Cachoeira_do_Parque_Nacional_da_Serra_dos_%C3%93rg%C3%A3os_Sede_Guapimirim.jpg/"
            + "1280px-Cachoeira_do_Parque_Nacional_da_Serra_dos_%C3%93rg%C3%A3os_Sede_Guapimirim.jpg";
    private static final String TEXTO_ALTERNATIVO_SEDE = "Cachoeira no Parque Nacional da Serra dos Órgãos, Sede Guapimirim. "
            + "Foto: Ferreiraandreza / Wikimedia Commons, CC BY-SA 3.0.";
    private static final String TEXTO_ALTERNATIVO_CAPELA = "Capela na sede Guapimirim do PARNASO. "
            + "Foto: Filipo tardim / Wikimedia Commons, CC BY-SA 4.0.";

    @Override
    @Transactional
    public void run(ApplicationArguments argumentos) {
        log.info("Iniciando dados iniciais do Guapi Verde.");
        // Serializa inicializadores de várias instâncias no mesmo PostgreSQL até o commit.
        entityManager.createNativeQuery("SELECT pg_advisory_xact_lock(71420260910)").getSingleResult();
        Map<String, Integer> adicionados = new LinkedHashMap<>();
        var natureza = categoria("Natureza", "Áreas naturais de Guapimirim.", adicionados);
        var patrimonio = categoria("Patrimônio histórico", "Construções históricas de Guapimirim.", adicionados);
        var sede = atrativo("Parque Nacional da Serra dos Órgãos - Sede Guapimirim", natureza,
                "Sede do PARNASO com poços e cachoeiras do rio Soberbo.",
                "Acesso pela BR-116, km 101, Guapimirim/RJ. Inclui a Capela de Nossa Senhora da Conceição do Soberbo. "
                        + "Acessibilidade não verificada: o indicador false nesta carga não constitui avaliação do local.",
                SituacaoAtrativo.ABERTO, adicionados);
        var capela = atrativo("Capela de Nossa Senhora da Conceição do Soberbo", patrimonio,
                "Construção histórica de 1713 em uma ilha do rio Soberbo.",
                "Capela barroca tombada pelo INEPAC, na sede Guapimirim do PARNASO. "
                        + "Capela e ponte fechadas para manutenção segundo o ICMBio na consulta de 10/09/2026.",
                SituacaoAtrativo.FECHADO_TEMPORARIAMENTE, adicionados);
        horarios(sede, adicionados);
        migrarImagemDaSede(sede);
        imagem(sede, FOTO_SEDE, TEXTO_ALTERNATIVO_SEDE, adicionados);
        imagem(capela, FOTO_CAPELA, TEXTO_ALTERNATIVO_CAPELA, adicionados);

        var inverno = temporada("Festival de Inverno de Guapimirim 2025", "2025-07-24", "2025-08-03", adicionados);
        var gospel = temporada("Final do Guapi Gospel Festival 2025", "2025-04-19", "2025-04-19", adicionados);
        evento("Abertura do VII Festival de Inverno de Guapimirim", inverno,
                "2025-07-24T19:00:00", "2025-07-24T22:00:00", "Casa de Viseu - Sede Campestre, Guapimirim/RJ",
                "Registro histórico: cerimônia de abertura com Raul Seixas - O Musical, em homenagem a Mauro Motta.", adicionados);
        evento("Final do Guapi Gospel Festival 2025", gospel,
                "2025-04-19T19:00:00", null, "Praça Paulo Terra, Centro, Guapimirim/RJ",
                "Registro histórico: final do festival de música gospel promovido pela Prefeitura de Guapimirim.", adicionados);

        var paraiso = parceiro("Paraíso da Serra", "https://www.paraisoserra.com/", "(21) 2040-3333",
                "Estabelecimento na Estrada Rio Teresópolis, km 90, Guapimirim/RJ.", adicionados);
        var pousada = parceiro("Pousada Sol Nascente", "https://pousadasolnascenteguapi.com.br/", "(21) 4040-1503",
                "Pousada na Estrada da Barreira, 577, Centro, Guapimirim/RJ.", adicionados);
        campanhaECupom(paraiso, "DEMO-GUAPI-01", adicionados);
        campanhaECupom(pousada, "DEMO-GUAPI-02", adicionados);

        var administrador = usuario("usuario.demo1@example.com", "Administrador de demonstração", PerfilUsuario.ADMIN, adicionados);
        var visitante = usuario("usuario.demo2@example.com", "Visitante de demonstração", PerfilUsuario.VISITANTE, adicionados);
        dadosDoUsuario(administrador, natureza, sede, adicionados);
        dadosDoUsuario(visitante, patrimonio, capela, adicionados);
        novidade(administrador, "DEMONSTRAÇÃO - Conheça a sede Guapimirim", sede.getResumo(), adicionados);
        novidade(administrador, "DEMONSTRAÇÃO - Patrimônio do rio Soberbo", capela.getResumo(), adicionados);
        entityManager.flush();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                log.info("Dados iniciais concluídos. Registros adicionados por entidade: {}", adicionados);
            }
        });
    }

    private CategoriaAtrativo categoria(String nome, String descricao, Map<String, Integer> adicionados) {
        return obterOuCriar(CategoriaAtrativo.class, "lower(e.nome) = lower(:nome)", Map.of("nome", nome), () -> {
            var dado = new CategoriaAtrativo();
            dado.setNome(nome);
            dado.setDescricao(descricao);
            return dado;
        }, adicionados);
    }

    private Atrativo atrativo(String nome, CategoriaAtrativo categoria, String resumo, String descricao,
            SituacaoAtrativo situacao, Map<String, Integer> adicionados) {
        return obterOuCriar(Atrativo.class, "lower(e.nome) = lower(:nome)", Map.of("nome", nome), () -> {
            var dado = new Atrativo();
            dado.setNome(nome);
            dado.setResumo(resumo);
            dado.setDescricao(descricao);
            dado.setCategoria(categoria);
            dado.setGratuito(true);
            dado.setAcessivel(false);
            dado.setSituacao(situacao);
            dado.setAtivo(categoria.getAtivo());
            return dado;
        }, adicionados);
    }

    private void horarios(Atrativo sede, Map<String, Integer> adicionados) {
        for (String dia : new String[]{"SEGUNDA", "TERCA", "QUARTA", "QUINTA", "SEXTA", "SABADO", "DOMINGO"}) {
            obterOuCriar(HorarioFuncionamento.class, "e.atrativo = :atrativo and lower(e.diaSemana) = lower(:dia)",
                    Map.of("atrativo", sede, "dia", dia), () -> {
                        var dado = new HorarioFuncionamento();
                        dado.setAtrativo(sede);
                        dado.setDiaSemana(dia);
                        dado.setFechado(dia.equals("SEGUNDA"));
                        if (!dado.getFechado()) {
                            dado.setHorarioAbertura(LocalTime.of(8, 0));
                            dado.setHorarioFechamento(LocalTime.of(17, 0));
                        }
                        dado.setObservacao("Entrada até 16h; saída até 17h. Segunda abre se feriado nacional/estadual RJ ou sua véspera. Fonte: ICMBio, PROV 01/2025.");
                        return dado;
                    }, adicionados);
        }
    }

    private void migrarImagemDaSede(Atrativo sede) {
        long existentes = entityManager.createQuery(
                "select count(e) from ImagemAtrativo e where e.atrativo = :sede and e.url = :url", Long.class)
                .setParameter("sede", sede)
                .setParameter("url", FOTO_SEDE)
                .getSingleResult();
        if (existentes > 0) {
            return;
        }
        entityManager.createQuery(
                "select e from ImagemAtrativo e where e.atrativo = :sede and e.url = :url order by e.id",
                ImagemAtrativo.class)
                .setParameter("sede", sede)
                .setParameter("url", FOTO_CAPELA)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst()
                .ifPresent(imagem -> {
                    imagem.setUrl(FOTO_SEDE);
                    imagem.setTextoAlternativo(TEXTO_ALTERNATIVO_SEDE);
                    log.info("Imagem inicial da sede atualizada na transação (id={}).", imagem.getId());
                });
    }

    private void imagem(Atrativo atrativo, String url, String textoAlternativo, Map<String, Integer> adicionados) {
        obterOuCriar(ImagemAtrativo.class, "e.atrativo = :atrativo and e.url = :url",
                Map.of("atrativo", atrativo, "url", url), () -> {
                    var dado = new ImagemAtrativo();
                    dado.setAtrativo(atrativo);
                    dado.setUrl(url);
                    dado.setTextoAlternativo(textoAlternativo);
                    long principais = entityManager.createQuery(
                            "select count(e) from ImagemAtrativo e where e.atrativo = :atrativo and e.principal = true", Long.class)
                            .setParameter("atrativo", atrativo).getSingleResult();
                    dado.setPrincipal(principais == 0);
                    dado.setOrdem(0);
                    return dado;
                }, adicionados);
    }

    private Temporada temporada(String nome, String inicio, String fim, Map<String, Integer> adicionados) {
        return obterOuCriar(Temporada.class, "lower(e.nome) = lower(:nome) and e.dataInicio = :inicio",
                Map.of("nome", nome, "inicio", LocalDate.parse(inicio)), () -> {
                    var dado = new Temporada();
                    dado.setNome(nome);
                    dado.setDescricao("Agrupamento histórico da programação: " + nome + ". Não representa agenda futura.");
                    dado.setDataInicio(LocalDate.parse(inicio));
                    dado.setDataFim(LocalDate.parse(fim));
                    return dado;
                }, adicionados);
    }

    private void evento(String nome, Temporada temporada, String inicio, String fim, String local,
            String descricao, Map<String, Integer> adicionados) {
        obterOuCriar(Evento.class, "lower(e.nome) = lower(:nome) and e.dataHoraInicio = :inicio",
                Map.of("nome", nome, "inicio", LocalDateTime.parse(inicio)), () -> {
                    var dado = new Evento();
                    dado.setNome(nome);
                    dado.setResumo(descricao);
                    dado.setDescricao(descricao);
                    dado.setTemporada(temporada);
                    dado.setDataHoraInicio(LocalDateTime.parse(inicio));
                    dado.setDataHoraFim(fim == null ? null : LocalDateTime.parse(fim));
                    dado.setLocal(local);
                    dado.setAtivo(temporada.getAtivo());
                    return dado;
                }, adicionados);
    }

    private Parceiro parceiro(String nome, String site, String telefone, String descricao, Map<String, Integer> adicionados) {
        return obterOuCriar(Parceiro.class, "lower(e.nome) = lower(:nome)", Map.of("nome", nome), () -> {
            var dado = new Parceiro();
            dado.setNome(nome);
            dado.setSite(site);
            dado.setTelefone(telefone);
            dado.setDescricao(descricao + " Cadastro de DEMONSTRAÇÃO baseado em informação pública; parceria com o Guapi Verde não confirmada.");
            dado.setAtivo(false);
            return dado;
        }, adicionados);
    }

    private void campanhaECupom(Parceiro parceiro, String codigo, Map<String, Integer> adicionados) {
        String titulo = "DEMONSTRAÇÃO - " + codigo;
        var campanha = obterOuCriar(Campanha.class, "e.parceiro = :parceiro and lower(e.titulo) = lower(:titulo)",
                Map.of("parceiro", parceiro, "titulo", titulo), () -> {
                    var dado = new Campanha();
                    dado.setParceiro(parceiro);
                    dado.setTitulo(titulo);
                    dado.setDescricao("DEMONSTRAÇÃO técnica sem valor comercial. Não é oferta do estabelecimento relacionado.");
                    dado.setDataInicio(LocalDate.of(2026, 1, 1));
                    dado.setDataFim(LocalDate.of(2026, 12, 31));
                    dado.setAtivo(false);
                    return dado;
                }, adicionados);
        obterOuCriar(Cupom.class, "lower(e.codigo) = lower(:codigo)", Map.of("codigo", codigo), () -> {
            var dado = new Cupom();
            dado.setCampanha(campanha);
            dado.setCodigo(codigo);
            dado.setDescricao("DEMONSTRAÇÃO sem desconto ou valor comercial; não emitido pelo estabelecimento.");
            dado.setRegrasUso("Exclusivo para testes locais do Guapi Verde. Não pode ser resgatado.");
            dado.setDataValidade(campanha.getDataFim());
            dado.setQuantidadeDisponivel(0);
            dado.setAtivo(false);
            return dado;
        }, adicionados);
    }

    private Usuario usuario(String email, String nome, PerfilUsuario perfil, Map<String, Integer> adicionados) {
        return obterOuCriar(Usuario.class, "lower(e.email) = lower(:email)", Map.of("email", email), () -> {
            var dado = new Usuario();
            dado.setNome(nome);
            dado.setEmail(email);
            // Credencial pública exclusiva de demonstração, bloqueada pelo perfil prod.
            dado.setSenha(codificadorDeSenha.encode("GuapiDemo!2026"));
            dado.setPerfil(perfil);
            return dado;
        }, adicionados);
    }

    private void dadosDoUsuario(Usuario usuario, CategoriaAtrativo categoria, Atrativo atrativo, Map<String, Integer> adicionados) {
        obterOuCriar(Preferencia.class, "e.usuario = :usuario and e.categoriaAtrativo = :categoria",
                Map.of("usuario", usuario, "categoria", categoria), () -> {
                    var dado = new Preferencia();
                    dado.setUsuario(usuario);
                    dado.setCategoriaAtrativo(categoria);
                    return dado;
                }, adicionados);
        // Qualquer decisão existente é preservada, inclusive revogações posteriores.
        obterOuCriar(Consentimento.class, "e.usuario = :usuario and e.tipo = :tipo",
                Map.of("usuario", usuario, "tipo", TipoConsentimento.TERMOS_USO),
                () -> new Consentimento(usuario, TipoConsentimento.TERMOS_USO, "DEMO-2026-01", false), adicionados);
        obterOuCriar(Favorito.class, "e.usuario = :usuario and e.atrativo = :atrativo",
                Map.of("usuario", usuario, "atrativo", atrativo), () -> {
                    var dado = new Favorito();
                    dado.setUsuario(usuario);
                    dado.setAtrativo(atrativo);
                    return dado;
                }, adicionados);
    }

    private void novidade(Usuario autor, String titulo, String resumo, Map<String, Integer> adicionados) {
        if (autor.getPerfil() != PerfilUsuario.ADMIN || !Boolean.TRUE.equals(autor.getAtivo())) {
            log.warn("Novidade não adicionada: o e-mail de demonstração já pertence a usuário sem perfil de administrador ativo.");
            return;
        }
        obterOuCriar(Novidade.class, "lower(e.titulo) = lower(:titulo)", Map.of("titulo", titulo), () -> {
            var dado = new Novidade();
            dado.setAutor(autor);
            dado.setTitulo(titulo);
            dado.setResumo(resumo);
            dado.setConteudo("Conteúdo editorial de DEMONSTRAÇÃO. " + resumo + " Fontes em docs/fontes-dados-iniciais.md.");
            dado.setSituacao(SituacaoNovidade.RASCUNHO);
            return dado;
        }, adicionados);
    }

    private <T> T obterOuCriar(Class<T> tipo, String criterio, Map<String, ?> parametros,
            Supplier<T> criar, Map<String, Integer> adicionados) {
        adicionados.putIfAbsent(tipo.getSimpleName(), 0);
        var consulta = entityManager.createQuery("select e from " + tipo.getSimpleName()
                + " e where " + criterio + " order by e.id", tipo);
        parametros.forEach(consulta::setParameter);
        var existente = consulta.setMaxResults(1).getResultList();
        if (!existente.isEmpty()) {
            return existente.getFirst();
        }
        T dado = criar.get();
        entityManager.persist(dado);
        adicionados.merge(tipo.getSimpleName(), 1, Integer::sum);
        log.info("Dado inicial incluído na transação: {} (id={})", tipo.getSimpleName(),
                entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(dado));
        return dado;
    }
}
