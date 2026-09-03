import { useCallback, useEffect, useMemo, useState } from 'react'
import { CalendarDays, Leaf, MapPin, Search } from 'lucide-react'
import { Link } from 'react-router-dom'
import CartaoAtrativo from '../../componentes/interface/CartaoAtrativo'
import ChipCategoria from '../../componentes/interface/ChipCategoria'
import TituloSecao from '../../componentes/interface/TituloSecao'
import { listarAtrativos } from '../../servicos/atrativoService'
import { listarCategoriasAtrativos } from '../../servicos/categoriaAtrativoService'
import { listarEventos } from '../../servicos/eventoService'
import { listarImagensDoAtrativo } from '../../servicos/imagemAtrativoService'

const estadoInicial = { dados: [], carregando: true, erro: false }

const formatadorDia = new Intl.DateTimeFormat('pt-BR', { day: '2-digit' })
const formatadorMes = new Intl.DateTimeFormat('pt-BR', { month: 'short' })
const formatadorData = new Intl.DateTimeFormat('pt-BR', {
  day: '2-digit',
  month: 'long',
  year: 'numeric',
})
const formatadorHorario = new Intl.DateTimeFormat('pt-BR', {
  hour: '2-digit',
  minute: '2-digit',
})

function selecionarImagem(imagens) {
  if (!Array.isArray(imagens) || imagens.length === 0) return null

  const principal = imagens.find((imagem) => imagem.principal)
  if (principal) return principal

  return [...imagens].sort(
    (primeira, segunda) => (primeira.ordem ?? Number.MAX_SAFE_INTEGER) - (segunda.ordem ?? Number.MAX_SAFE_INTEGER),
  )[0]
}

function EstadoSecao({ carregando, erro, vazio, mensagemVazia, aoTentarNovamente, children }) {
  if (carregando) {
    return <p className="rounded-2xl bg-white p-5 text-slate-600 shadow-sm">Carregando experiências...</p>
  }

  if (erro) {
    return (
      <div className="rounded-2xl border border-red-200 bg-white p-5 text-slate-700 shadow-sm" role="alert">
        <p>Não foi possível carregar os dados.</p>
        <button
          type="button"
          onClick={aoTentarNovamente}
          className="mt-3 rounded-full bg-floresta px-5 py-2.5 font-bold text-white transition hover:bg-folha"
        >
          Tentar novamente
        </button>
      </div>
    )
  }

  if (vazio) {
    return <p className="rounded-2xl bg-white p-5 text-slate-600 shadow-sm">{mensagemVazia}</p>
  }

  return children
}

function Inicio() {
  const [categorias, setCategorias] = useState(estadoInicial)
  const [atrativos, setAtrativos] = useState(estadoInicial)
  const [eventos, setEventos] = useState(estadoInicial)
  const [agora] = useState(() => Date.now())

  const carregarCategorias = useCallback(async () => {
    try {
      const dados = await listarCategoriasAtrativos()
      setCategorias({ dados: Array.isArray(dados) ? dados : [], carregando: false, erro: false })
    } catch {
      setCategorias({ dados: [], carregando: false, erro: true })
    }
  }, [])

  const carregarAtrativos = useCallback(async () => {
    try {
      const resposta = await listarAtrativos()
      const destaques = (Array.isArray(resposta) ? resposta : []).slice(0, 3)
      const destaquesComImagens = await Promise.all(
        destaques.map(async (atrativo) => {
          try {
            const imagens = await listarImagensDoAtrativo(atrativo.id)
            return { ...atrativo, imagemSelecionada: selecionarImagem(imagens) }
          } catch {
            return { ...atrativo, imagemSelecionada: null }
          }
        }),
      )
      setAtrativos({ dados: destaquesComImagens, carregando: false, erro: false })
    } catch {
      setAtrativos({ dados: [], carregando: false, erro: true })
    }
  }, [])

  const carregarEventos = useCallback(async () => {
    try {
      const dados = await listarEventos()
      setEventos({ dados: Array.isArray(dados) ? dados : [], carregando: false, erro: false })
    } catch {
      setEventos({ dados: [], carregando: false, erro: true })
    }
  }, [])

  useEffect(() => {
    carregarCategorias()
    carregarAtrativos()
    carregarEventos()
  }, [carregarAtrativos, carregarCategorias, carregarEventos])

  const proximoEvento = useMemo(() => {
    return [...eventos.dados]
      .filter((evento) => {
        const encerramento = new Date(evento.dataHoraFim).getTime()
        return Number.isFinite(encerramento) && encerramento >= agora
      })
      .sort((primeiro, segundo) => new Date(primeiro.dataHoraInicio) - new Date(segundo.dataHoraInicio))[0]
  }, [agora, eventos.dados])

  const tentarNovamenteCategorias = () => {
    setCategorias((estado) => ({ ...estado, carregando: true, erro: false }))
    carregarCategorias()
  }

  const tentarNovamenteAtrativos = () => {
    setAtrativos((estado) => ({ ...estado, carregando: true, erro: false }))
    carregarAtrativos()
  }

  const tentarNovamenteEventos = () => {
    setEventos((estado) => ({ ...estado, carregando: true, erro: false }))
    carregarEventos()
  }

  const imagemHeroi = atrativos.dados.find((atrativo) => atrativo.imagemSelecionada)?.imagemSelecionada
  const dataEvento = proximoEvento ? new Date(proximoEvento.dataHoraInicio) : null

  return (
    <>
      <section
        className="relative isolate flex min-h-[28rem] items-end overflow-hidden bg-floresta px-4 py-10 text-white sm:px-6 md:min-h-[32rem] md:rounded-b-[2.5rem] md:px-10"
        style={
          imagemHeroi
            ? {
                backgroundImage: `linear-gradient(180deg, rgba(10, 42, 29, 0.12) 10%, rgba(10, 42, 29, 0.9) 92%), url(${imagemHeroi.url})`,
                backgroundPosition: 'center',
                backgroundSize: 'cover',
              }
            : undefined
        }
        aria-label={imagemHeroi?.textoAlternativo || 'Guapi Verde'}
      >
        {!imagemHeroi && <Leaf className="absolute right-8 top-8 text-white/10" size={160} aria-hidden="true" />}
        <div className="w-full max-w-2xl">
          <p className="mb-3 inline-flex items-center gap-2 rounded-full bg-white/15 px-3 py-1.5 text-sm font-semibold backdrop-blur-sm">
            <MapPin size={16} aria-hidden="true" /> Guapimirim, Rio de Janeiro
          </p>
          <h1 className="max-w-xl text-4xl font-bold leading-tight tracking-tight sm:text-5xl">
            Guapimirim, natureza que inspira e acolhe.
          </h1>
          <Link
            to="/explorar"
            className="mt-7 flex min-h-14 w-full items-center gap-3 rounded-2xl bg-white px-4 text-left text-slate-500 shadow-xl transition hover:bg-creme sm:max-w-lg"
            aria-label="Pesquisar atrativos"
          >
            <Search className="text-folha" size={22} aria-hidden="true" />
            <span>O que você quer conhecer?</span>
          </Link>
        </div>
      </section>

      <div className="space-y-10 px-4 py-8 sm:px-6 md:space-y-14 md:py-12">
        <section aria-labelledby="atalhos-titulo">
          <h2 id="atalhos-titulo" className="mb-4 text-xl font-bold text-floresta">
            Explore seu próximo destino
          </h2>
          <EstadoSecao carregando={categorias.carregando} erro={categorias.erro} vazio={categorias.dados.length === 0} mensagemVazia="Nenhuma categoria disponível." aoTentarNovamente={tentarNovamenteCategorias}>
            <div className="flex gap-3 overflow-x-auto pb-2">
              {categorias.dados.map((categoria) => (
                <ChipCategoria key={categoria.id} {...categoria} Icone={Leaf} />
              ))}
            </div>
          </EstadoSecao>
        </section>

        <section aria-labelledby="destaques-titulo">
          <div id="destaques-titulo"><TituloSecao titulo="Destaques" caminho="/explorar" /></div>
          <EstadoSecao carregando={atrativos.carregando} erro={atrativos.erro} vazio={atrativos.dados.length === 0} mensagemVazia="Nenhum atrativo cadastrado ainda." aoTentarNovamente={tentarNovamenteAtrativos}>
            <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
              {atrativos.dados.map((atrativo) => <CartaoAtrativo key={atrativo.id} atrativo={atrativo} />)}
            </div>
          </EstadoSecao>
        </section>

        <section aria-labelledby="eventos-titulo">
          <div id="eventos-titulo"><TituloSecao titulo="Próximos eventos" caminho="/agenda" /></div>
          <EstadoSecao carregando={eventos.carregando} erro={eventos.erro} vazio={!proximoEvento} mensagemVazia="Nenhum evento programado no momento." aoTentarNovamente={tentarNovamenteEventos}>
            {proximoEvento && (
              <article className="flex items-center gap-4 overflow-hidden rounded-3xl bg-floresta p-4 text-white shadow-lg sm:p-6">
                {proximoEvento.imagemUrl && <img src={proximoEvento.imagemUrl} alt="" className="hidden size-24 rounded-2xl object-cover sm:block" onError={(evento) => { evento.currentTarget.hidden = true }} />}
                <div className="grid min-w-16 place-items-center rounded-2xl bg-dourado px-3 py-2 text-floresta">
                  <strong className="text-2xl leading-none">{formatadorDia.format(dataEvento)}</strong>
                  <span className="text-xs font-bold uppercase">{formatadorMes.format(dataEvento)}</span>
                </div>
                <div className="min-w-0 flex-1">
                  <h3 className="font-bold sm:text-lg">{proximoEvento.nome}</h3>
                  {proximoEvento.resumo && <p className="mt-1 text-sm text-white/75">{proximoEvento.resumo}</p>}
                  <p className="mt-2 flex flex-wrap items-center gap-x-3 gap-y-1 text-sm text-white/75">
                    <span className="flex items-center gap-1"><CalendarDays size={15} aria-hidden="true" /> {formatadorData.format(dataEvento)}, às {formatadorHorario.format(dataEvento)}</span>
                    {proximoEvento.local && <span className="flex items-center gap-1"><MapPin size={15} aria-hidden="true" /> {proximoEvento.local}</span>}
                  </p>
                </div>
              </article>
            )}
          </EstadoSecao>
        </section>
      </div>
    </>
  )
}

export default Inicio
