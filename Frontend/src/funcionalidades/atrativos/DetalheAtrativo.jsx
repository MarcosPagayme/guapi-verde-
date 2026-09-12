import { useEffect, useState } from 'react'
import { ArrowLeft, Leaf } from 'lucide-react'
import { Link, useParams } from 'react-router-dom'
import { obterAtrativoPorId } from '../../servicos/atrativoService'
import { listarImagensDoAtrativo } from '../../servicos/imagemAtrativoService'
import { listarHorariosDoAtrativo } from '../../servicos/horarioFuncionamentoService'
import BotaoFavorito from './BotaoFavorito'

const painel = 'rounded-3xl border border-floresta/10 bg-white p-5 shadow-sm sm:p-7'
const botao = 'inline-flex min-h-11 items-center justify-center gap-2 rounded-full bg-floresta px-5 py-2.5 font-bold text-white transition hover:bg-folha'
const moeda = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const situacoes = { ABERTO: 'Aberto', FECHADO_TEMPORARIAMENTE: 'Fechado temporariamente', INDISPONIVEL: 'Indisponível' }
const dias = ['Segunda-feira', 'Terça-feira', 'Quarta-feira', 'Quinta-feira', 'Sexta-feira', 'Sábado', 'Domingo']

function indiceDia(dia) {
  const normalizado = (dia ?? '').normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase().trim()
  return ['segunda', 'terca', 'quarta', 'quinta', 'sexta', 'sabado', 'domingo'].findIndex((nome) => normalizado.startsWith(nome))
}

function siteSeguro(site) {
  if (!site?.trim()) return null
  try {
    const url = new URL(/^[a-z][a-z\d+.-]*:/i.test(site.trim()) ? site.trim() : `https://${site.trim()}`)
    return ['http:', 'https:'].includes(url.protocol) ? url.href : null
  } catch {
    return null
  }
}

function ImagemDetalhe({ imagem, nome, destaque = false }) {
  const [falhou, setFalhou] = useState(false)
  return (
    <div className={`relative grid place-items-center overflow-hidden rounded-3xl bg-folha/10 text-folha ${destaque ? 'aspect-[4/3] sm:aspect-[16/7]' : 'aspect-[4/3]'}`}>
      {imagem?.url && !falhou ? (
        <img src={imagem.url} alt={imagem.textoAlternativo || `Imagem de ${nome || 'atrativo'}`}
          loading={destaque ? 'eager' : 'lazy'} className="absolute inset-0 size-full object-cover" onError={() => setFalhou(true)} />
      ) : <div role="img" aria-label="Imagem indisponível"><Leaf size={48} aria-hidden="true" /></div>}
    </div>
  )
}

function HorariosDetalhe({ estado }) {
  const grupos = new Map()
  for (const horario of estado.dados) {
    const indice = indiceDia(horario.diaSemana)
    const nome = indice < 0 ? horario.diaSemana || 'Dia não informado' : dias[indice]
    if (!grupos.has(nome)) grupos.set(nome, { nome, ordem: indice < 0 ? 7 : indice, horarios: [] })
    grupos.get(nome).horarios.push(horario)
  }
  return (
    <section className={painel} aria-labelledby="titulo-horarios">
      <h2 id="titulo-horarios" className="text-xl font-bold text-floresta">Horários de funcionamento</h2>
      {estado.carregando ? <p role="status" className="mt-4 text-slate-600">Carregando horários...</p>
        : estado.erro ? <p role="status" className="mt-4 text-slate-600">Não foi possível carregar os horários. Tente novamente mais tarde.</p>
          : grupos.size === 0 ? <p className="mt-4 text-slate-600">Horários não informados.</p>
            : <dl className="mt-4 divide-y divide-floresta/10">
              {[...grupos.values()].sort((a, b) => a.ordem - b.ordem).map((grupo) => (
                <div key={grupo.nome} className="py-3 first:pt-0">
                  <dt className="font-semibold text-slate-900">{grupo.nome}</dt>
                  {grupo.horarios.sort((a, b) => (a.horarioAbertura ?? '').localeCompare(b.horarioAbertura ?? '')).map((horario, indice) => (
                    <dd key={horario.id ?? indice} className="mt-1 text-slate-600">
                      <p>{horario.fechado === true ? 'Fechado'
                        : horario.horarioAbertura && horario.horarioFechamento ? `${horario.horarioAbertura.slice(0, 5)} às ${horario.horarioFechamento.slice(0, 5)}`
                          : horario.horarioAbertura ? `A partir das ${horario.horarioAbertura.slice(0, 5)}`
                            : horario.horarioFechamento ? `Até ${horario.horarioFechamento.slice(0, 5)}` : 'Horário não informado'}</p>
                      {horario.observacao && <p className="mt-1 whitespace-pre-line text-sm">{horario.observacao}</p>}
                    </dd>
                  ))}
                </div>
              ))}
            </dl>}
    </section>
  )
}

function ConteudoDetalhe({ id }) {
  const [tentativa, setTentativa] = useState(0)
  const [atrativo, setAtrativo] = useState({ carregando: true })
  const [imagens, setImagens] = useState({ dados: [], carregando: true })
  const [horarios, setHorarios] = useState({ dados: [], carregando: true })

  useEffect(() => {
    let ativo = true
    obterAtrativoPorId(id).then((dados) => {
      if (ativo) setAtrativo({ dados, naoEncontrado: !dados, carregando: false })
    }).catch((erro) => {
      if (ativo) setAtrativo({ carregando: false, erro: true, naoEncontrado: erro.response?.status === 404 })
    })
    const carregarLista = async (consultar, atualizar) => {
      try {
        const dados = await consultar(id)
        if (ativo) atualizar({ dados: Array.isArray(dados) ? dados.filter(Boolean) : [], carregando: false })
      } catch {
        if (ativo) atualizar({ dados: [], carregando: false, erro: true })
      }
    }
    carregarLista(listarImagensDoAtrativo, setImagens)
    carregarLista(listarHorariosDoAtrativo, setHorarios)
    return () => { ativo = false }
  }, [id, tentativa])

  function tentarNovamente() {
    setAtrativo({ carregando: true })
    setImagens({ dados: [], carregando: true })
    setHorarios({ dados: [], carregando: true })
    setTentativa((valor) => valor + 1)
  }

  if (atrativo.carregando) return <p role="status" className={painel}>Carregando atrativo...</p>
  if (atrativo.naoEncontrado) return (
    <div className={painel} role="status">
      <h1 className="text-2xl font-bold text-floresta">Atrativo não encontrado</h1>
      <p className="mt-3 text-slate-600">Este atrativo não está disponível. Volte para Explorar e conheça outros lugares.</p>
    </div>
  )
  if (atrativo.erro) return (
    <div className={painel} role="alert">
      <h1 className="text-2xl font-bold text-floresta">Não foi possível carregar o atrativo</h1>
      <p className="mb-5 mt-3 text-slate-600">Tente novamente para consultar as informações deste lugar.</p>
      <button type="button" className={botao} onClick={tentarNovamente}>Tentar novamente</button>
    </div>
  )

  const dados = atrativo.dados
  const ordenadas = [...imagens.dados].sort((a, b) => Number(b.principal === true) - Number(a.principal === true)
    || (a.ordem ?? Number.MAX_SAFE_INTEGER) - (b.ordem ?? Number.MAX_SAFE_INTEGER))
  const site = siteSeguro(dados.site)
  const coordenadas = [dados.latitude, dados.longitude]
  const temCoordenadas = coordenadas.every((valor) => valor != null && String(valor).trim() !== '' && Number.isFinite(Number(valor)))
    && Math.abs(Number(dados.latitude)) <= 90 && Math.abs(Number(dados.longitude)) <= 180
  const informacoes = [
    ['Endereço', dados.endereco],
    ['Telefone', dados.telefone && <a key="telefone" className="underline" href={`tel:${dados.telefone.replace(/[^\d+]/g, '')}`}>{dados.telefone}</a>],
    ['Site', site && <a key="site" className="underline" href={site} target="_blank" rel="noopener noreferrer">Visitar site (abre em nova aba)</a>],
    ['Entrada', dados.gratuito === true ? 'Gratuita' : dados.gratuito === false ? 'Paga' : null],
    ['Valor da entrada', dados.gratuito === false && dados.valorEntrada != null && String(dados.valorEntrada).trim() !== '' && Number.isFinite(Number(dados.valorEntrada)) ? moeda.format(Number(dados.valorEntrada)) : null],
    ['Acessibilidade', dados.acessivel === true ? 'Acessível' : dados.acessivel === false ? 'Não acessível' : null],
    ['Situação', situacoes[dados.situacao] || dados.situacao],
  ].filter(([, valor]) => valor)

  return (
    <article className="space-y-6">
      <header>
        {dados.categoria?.nome && <p className="font-bold text-floresta">{dados.categoria.nome}</p>}
        {dados.nome && <h1 className="mt-2 text-3xl font-bold text-floresta sm:text-4xl">{dados.nome}</h1>}
        {dados.resumo && <p className="mt-3 max-w-3xl whitespace-pre-line text-lg text-slate-600">{dados.resumo}</p>}
        <BotaoFavorito atrativoId={id} />
      </header>
      <section aria-label="Imagens do atrativo" className="space-y-3">
        <ImagemDetalhe key={ordenadas[0]?.url ?? 'sem-imagem'} imagem={ordenadas[0]} nome={dados.nome} destaque />
        {imagens.carregando && <p role="status" className="text-sm text-slate-600">Carregando imagens...</p>}
        {imagens.erro && <p role="status" className="text-sm text-slate-600">Não foi possível carregar as imagens.</p>}
        {ordenadas.length > 1 && <div className="grid grid-cols-2 gap-3 sm:grid-cols-3">
          {ordenadas.slice(1).map((imagem, indice) => <ImagemDetalhe key={`${imagem.id ?? indice}-${imagem.url}`} imagem={imagem} nome={dados.nome} />)}
        </div>}
      </section>
      <div className="grid items-start gap-6 lg:grid-cols-[minmax(0,3fr)_minmax(0,2fr)]">
        <div className="min-w-0 space-y-6">
          {dados.descricao && <section className={painel} aria-labelledby="titulo-sobre">
            <h2 id="titulo-sobre" className="text-xl font-bold text-floresta">Sobre o atrativo</h2>
            <p className="mt-4 whitespace-pre-line leading-relaxed text-slate-600">{dados.descricao}</p>
          </section>}
          {(informacoes.length > 0 || temCoordenadas) && <section className={painel} aria-labelledby="titulo-visita">
            <h2 id="titulo-visita" className="text-xl font-bold text-floresta">Planeje sua visita</h2>
            <dl className="mt-4 space-y-4">
              {informacoes.map(([rotulo, valor]) => <div key={rotulo}>
                <dt className="font-semibold text-slate-900">{rotulo}</dt>
                <dd className="mt-1 whitespace-pre-line text-slate-600">{valor}</dd>
              </div>)}
            </dl>
            {temCoordenadas && <a className={`${botao} mt-5`} href={`https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(coordenadas.join(','))}`} target="_blank" rel="noopener noreferrer" aria-label="Como chegar pelo Google Maps (abre em nova aba)">Como chegar</a>}
          </section>}
        </div>
        <HorariosDetalhe estado={horarios} />
      </div>
    </article>
  )
}

function DetalheAtrativo() {
  const { id } = useParams()
  return (
    <div className="break-words px-4 py-8 sm:px-6 sm:py-12">
      <Link to="/explorar" className={`${botao} mb-6`}><ArrowLeft size={18} aria-hidden="true" />Voltar para Explorar</Link>
      <ConteudoDetalhe key={id} id={id} />
    </div>
  )
}

export default DetalheAtrativo
