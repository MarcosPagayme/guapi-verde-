import { useCallback, useEffect, useState } from 'react'
import { CalendarDays, ExternalLink, Handshake, Megaphone } from 'lucide-react'
import { listarCampanhas } from '../../servicos/campanhaService'
import { listarParceiros } from '../../servicos/parceiroService'

const estadoInicial = { dados: [], carregando: true, erro: false }

const formatadorData = new Intl.DateTimeFormat('pt-BR', {
  day: '2-digit',
  month: '2-digit',
  year: 'numeric',
})

function formatarData(data) {
  const dataFormatada = new Date(data)
  return Number.isNaN(dataFormatada.getTime()) ? null : formatadorData.format(dataFormatada)
}

function EstadoSecao({ carregando, erro, vazio, mensagemVazia, aoTentarNovamente, children }) {
  if (carregando) {
    return <p className="rounded-2xl bg-white p-5 text-slate-600 shadow-sm">Carregando...</p>
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

function CartaoParceiro({ parceiro }) {
  const logo = parceiro.logo || parceiro.logoUrl
  const site = parceiro.site || parceiro.siteUrl

  return (
    <article className="flex h-full flex-col rounded-3xl bg-white p-5 shadow-[0_8px_24px_rgba(23,77,54,0.09)]">
      <div className="grid aspect-[3/1] place-items-center overflow-hidden rounded-2xl bg-creme p-4 text-folha">
        <Handshake size={42} aria-hidden="true" />
        {logo && (
          <img
            src={logo}
            alt={parceiro.nome ? `Logo de ${parceiro.nome}` : 'Logo do parceiro'}
            className="max-h-full max-w-full object-contain"
            onError={(evento) => {
              evento.currentTarget.hidden = true
            }}
          />
        )}
      </div>
      <h3 className="mt-5 text-lg font-bold text-floresta">{parceiro.nome || 'Parceiro'}</h3>
      {parceiro.descricao && <p className="mt-2 flex-1 text-sm leading-relaxed text-slate-600">{parceiro.descricao}</p>}
      {site && (
        <a
          href={site}
          target="_blank"
          rel="noopener noreferrer"
          className="mt-5 inline-flex items-center gap-2 self-start font-bold text-folha hover:underline"
        >
          Visitar site <ExternalLink size={16} aria-hidden="true" />
        </a>
      )}
    </article>
  )
}

function CartaoCampanha({ campanha }) {
  const imagem = campanha.imagem || campanha.imagemUrl
  const dataInicio = formatarData(campanha.dataInicio)
  const dataFim = formatarData(campanha.dataFim)
  const periodo = dataInicio && dataFim ? `${dataInicio} a ${dataFim}` : dataInicio || dataFim

  return (
    <article className="overflow-hidden rounded-3xl bg-white shadow-[0_8px_24px_rgba(23,77,54,0.09)]">
      <div className="relative grid aspect-[16/8] place-items-center overflow-hidden bg-floresta/10 text-floresta">
        <Megaphone size={42} aria-hidden="true" />
        {imagem && (
          <img
            src={imagem}
            alt={campanha.titulo ? `Imagem da campanha ${campanha.titulo}` : 'Imagem da campanha'}
            className="absolute inset-0 size-full object-cover"
            onError={(evento) => {
              evento.currentTarget.hidden = true
            }}
          />
        )}
      </div>
      <div className="p-5">
        <h3 className="text-lg font-bold text-floresta">{campanha.titulo || 'Campanha'}</h3>
        {campanha.descricao && <p className="mt-2 text-sm leading-relaxed text-slate-600">{campanha.descricao}</p>}
        <p className="mt-4 flex items-center gap-2 text-sm font-semibold text-folha">
          <CalendarDays size={17} aria-hidden="true" />
          <span>{periodo || 'Período não informado'}</span>
        </p>
      </div>
    </article>
  )
}

function Beneficios() {
  const [parceiros, setParceiros] = useState(estadoInicial)
  const [campanhas, setCampanhas] = useState(estadoInicial)

  const carregarParceiros = useCallback(async () => {
    try {
      const dados = await listarParceiros()
      setParceiros({ dados: Array.isArray(dados) ? dados : [], carregando: false, erro: false })
    } catch {
      setParceiros({ dados: [], carregando: false, erro: true })
    }
  }, [])

  const carregarCampanhas = useCallback(async () => {
    try {
      const dados = await listarCampanhas()
      setCampanhas({ dados: Array.isArray(dados) ? dados : [], carregando: false, erro: false })
    } catch {
      setCampanhas({ dados: [], carregando: false, erro: true })
    }
  }, [])

  useEffect(() => {
    const carregarDados = async () => {
      await Promise.all([carregarParceiros(), carregarCampanhas()])
    }

    carregarDados()
  }, [carregarCampanhas, carregarParceiros])

  const tentarNovamenteParceiros = () => {
    setParceiros((estado) => ({ ...estado, carregando: true, erro: false }))
    carregarParceiros()
  }

  const tentarNovamenteCampanhas = () => {
    setCampanhas((estado) => ({ ...estado, carregando: true, erro: false }))
    carregarCampanhas()
  }

  return (
    <div className="space-y-10 px-4 py-8 sm:px-6 md:space-y-14 md:px-10 md:py-12">
      <header>
        <p className="text-sm font-bold uppercase tracking-[0.2em] text-folha">Guapi Verde</p>
        <h1 className="mt-2 text-3xl font-bold tracking-tight text-floresta sm:text-4xl">Benefícios</h1>
        <p className="mt-3 max-w-2xl leading-relaxed text-slate-600">Conheça parceiros e campanhas disponíveis para sua experiência em Guapimirim.</p>
      </header>

      <section aria-labelledby="parceiros-titulo">
        <h2 id="parceiros-titulo" className="mb-4 text-xl font-bold text-floresta sm:text-2xl">Parceiros</h2>
        <EstadoSecao carregando={parceiros.carregando} erro={parceiros.erro} vazio={parceiros.dados.length === 0} mensagemVazia="Nenhum parceiro disponível no momento." aoTentarNovamente={tentarNovamenteParceiros}>
          <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
            {parceiros.dados.map((parceiro, indice) => <CartaoParceiro key={parceiro.id ?? indice} parceiro={parceiro} />)}
          </div>
        </EstadoSecao>
      </section>

      <section aria-labelledby="campanhas-titulo">
        <h2 id="campanhas-titulo" className="mb-4 text-xl font-bold text-floresta sm:text-2xl">Campanhas</h2>
        <EstadoSecao carregando={campanhas.carregando} erro={campanhas.erro} vazio={campanhas.dados.length === 0} mensagemVazia="Nenhuma campanha disponível no momento." aoTentarNovamente={tentarNovamenteCampanhas}>
          <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
            {campanhas.dados.map((campanha, indice) => <CartaoCampanha key={campanha.id ?? indice} campanha={campanha} />)}
          </div>
        </EstadoSecao>
      </section>
    </div>
  )
}

export default Beneficios
