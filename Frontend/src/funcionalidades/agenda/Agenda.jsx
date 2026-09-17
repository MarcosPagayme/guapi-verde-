import { useEffect, useState } from 'react'
import { CalendarDays, Clock3, Leaf, MapPin } from 'lucide-react'
import { listarEventos } from '../../servicos/eventoService'
import { listarTemporadas } from '../../servicos/temporadaService'

const estadoInicial = { dados: [], carregando: true, erro: false }

const formatadorData = new Intl.DateTimeFormat('pt-BR', {
  day: '2-digit',
  month: 'long',
  year: 'numeric',
})

const formatadorHorario = new Intl.DateTimeFormat('pt-BR', {
  hour: '2-digit',
  minute: '2-digit',
})

function criarData(valor) {
  if (!valor) return null

  const valorLocal = /^\d{4}-\d{2}-\d{2}$/.test(valor) ? `${valor}T00:00:00` : valor
  const data = new Date(valorLocal)
  return Number.isNaN(data.getTime()) ? null : data
}

function EstadoLista({ carregando, erro, vazia, mensagemVazia, aoTentarNovamente, children }) {
  if (carregando) {
    return <p className="rounded-2xl bg-white p-5 text-slate-600 shadow-sm">Carregando...</p>
  }

  if (erro) {
    return (
      <div className="rounded-2xl border border-red-200 bg-white p-5 text-slate-700 shadow-sm" role="alert">
        <p>Não foi possível carregar esta lista.</p>
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

  if (vazia) {
    return <p className="rounded-2xl bg-white p-5 text-slate-600 shadow-sm">{mensagemVazia}</p>
  }

  return children
}

function CartaoEvento({ evento }) {
  const dataInicio = criarData(evento.dataHoraInicio)
  const dataFim = criarData(evento.dataHoraFim)

  return (
    <article className="overflow-hidden rounded-3xl bg-white shadow-[0_8px_24px_rgba(23,77,54,0.09)]">
      {evento.imagemUrl && (
        <img
          src={evento.imagemUrl}
          alt={`Imagem de ${evento.nome || 'evento'}`}
          className="aspect-[16/8] w-full object-cover"
          onError={(event) => {
            event.currentTarget.hidden = true
          }}
        />
      )}
      <div className="p-5">
        {evento.nome && <h3 className="text-lg font-bold text-floresta">{evento.nome}</h3>}
        {evento.resumo && <p className="mt-2 text-sm leading-6 text-slate-600">{evento.resumo}</p>}
        <div className="mt-4 space-y-2 text-sm text-slate-600">
          {dataInicio && (
            <p className="flex items-start gap-2">
              <CalendarDays className="mt-0.5 shrink-0 text-folha" size={17} aria-hidden="true" />
              <span>
                {formatadorData.format(dataInicio)}
                {dataFim && ` a ${formatadorData.format(dataFim)}`}
              </span>
            </p>
          )}
          {dataInicio && (
            <p className="flex items-start gap-2">
              <Clock3 className="mt-0.5 shrink-0 text-folha" size={17} aria-hidden="true" />
              <span>
                {formatadorHorario.format(dataInicio)}
                {dataFim && ` ate ${formatadorHorario.format(dataFim)}`}
              </span>
            </p>
          )}
          {evento.local && (
            <p className="flex items-start gap-2">
              <MapPin className="mt-0.5 shrink-0 text-folha" size={17} aria-hidden="true" />
              <span>{evento.local}</span>
            </p>
          )}
        </div>
      </div>
    </article>
  )
}

function CartaoTemporada({ temporada }) {
  const dataInicio = criarData(temporada.dataInicio)
  const dataFim = criarData(temporada.dataFim)

  return (
    <article className="rounded-3xl border border-floresta/10 bg-white p-5 shadow-[0_8px_24px_rgba(23,77,54,0.09)]">
      <div className="flex items-start gap-3">
        <span className="grid size-11 shrink-0 place-items-center rounded-2xl bg-folha/10 text-folha">
          <Leaf size={22} aria-hidden="true" />
        </span>
        <div className="min-w-0">
          {temporada.nome && <h3 className="text-lg font-bold text-floresta">{temporada.nome}</h3>}
          {temporada.descricao && <p className="mt-2 text-sm leading-6 text-slate-600">{temporada.descricao}</p>}
        </div>
      </div>
      {(dataInicio || dataFim) && (
        <p className="mt-4 flex items-start gap-2 text-sm text-slate-600">
          <CalendarDays className="mt-0.5 shrink-0 text-folha" size={17} aria-hidden="true" />
          <span>
            {dataInicio && formatadorData.format(dataInicio)}
            {dataInicio && dataFim && ' a '}
            {dataFim && formatadorData.format(dataFim)}
          </span>
        </p>
      )}
    </article>
  )
}

function Agenda() {
  const [abaAtiva, setAbaAtiva] = useState('eventos')
  const [eventos, setEventos] = useState(estadoInicial)
  const [temporadas, setTemporadas] = useState(estadoInicial)

  const carregarEventos = async () => {
    setEventos((estado) => ({ ...estado, carregando: true, erro: false }))
    try {
      const dados = await listarEventos()
      setEventos({ dados: Array.isArray(dados) ? dados : [], carregando: false, erro: false })
    } catch {
      setEventos({ dados: [], carregando: false, erro: true })
    }
  }

  const carregarTemporadas = async () => {
    setTemporadas((estado) => ({ ...estado, carregando: true, erro: false }))
    try {
      const dados = await listarTemporadas()
      setTemporadas({ dados: Array.isArray(dados) ? dados : [], carregando: false, erro: false })
    } catch {
      setTemporadas({ dados: [], carregando: false, erro: true })
    }
  }

  useEffect(() => {
    let montado = true

    listarEventos()
      .then((dados) => {
        if (montado) setEventos({ dados: Array.isArray(dados) ? dados : [], carregando: false, erro: false })
      })
      .catch(() => {
        if (montado) setEventos({ dados: [], carregando: false, erro: true })
      })

    listarTemporadas()
      .then((dados) => {
        if (montado) setTemporadas({ dados: Array.isArray(dados) ? dados : [], carregando: false, erro: false })
      })
      .catch(() => {
        if (montado) setTemporadas({ dados: [], carregando: false, erro: true })
      })

    return () => {
      montado = false
    }
  }, [])

  const [agora] = useState(() => Date.now())
  const eventosVisiveis = eventos.dados
    .filter((evento) => {
      const inicio = criarData(evento.dataHoraInicio)
      const fim = criarData(evento.dataHoraFim)
      if (!inicio) return false
      return fim ? fim.getTime() >= agora : inicio.getTime() >= agora
    })
    .sort((primeiro, segundo) => criarData(primeiro.dataHoraInicio) - criarData(segundo.dataHoraInicio))

  const temporadasOrdenadas = [...temporadas.dados].sort((primeira, segunda) => {
    const dataPrimeira = criarData(primeira.dataInicio)?.getTime() ?? Number.MAX_SAFE_INTEGER
    const dataSegunda = criarData(segunda.dataInicio)?.getTime() ?? Number.MAX_SAFE_INTEGER
    return dataPrimeira - dataSegunda
  })

  return (
    <section className="px-4 py-8 sm:px-6 sm:py-12">
      <p className="font-bold uppercase tracking-[0.18em] text-folha">Guapi Verde</p>
      <h1 className="mt-2 text-3xl font-bold text-floresta sm:text-4xl">Agenda</h1>
      <p className="mt-3 max-w-2xl text-slate-600">Acompanhe os eventos e as temporadas de Guapimirim.</p>

      <div className="mt-8 border-b border-floresta/15" role="tablist" aria-label="Conteúdo da agenda">
        <div className="flex gap-6">
          {[
            { id: 'eventos', titulo: 'Eventos' },
            { id: 'temporadas', titulo: 'Temporadas' },
          ].map((aba) => (
            <button
              key={aba.id}
              type="button"
              role="tab"
              id={`aba-${aba.id}`}
              aria-selected={abaAtiva === aba.id}
              aria-controls={`painel-${aba.id}`}
              onClick={() => setAbaAtiva(aba.id)}
              className={`border-b-2 px-1 pb-3 text-sm font-bold transition sm:text-base ${
                abaAtiva === aba.id ? 'border-floresta text-floresta' : 'border-transparent text-slate-500 hover:text-floresta'
              }`}
            >
              {aba.titulo}
            </button>
          ))}
        </div>
      </div>

      <div className="mt-6" role="tabpanel" id={`painel-${abaAtiva}`} aria-labelledby={`aba-${abaAtiva}`}>
        {abaAtiva === 'eventos' ? (
          <EstadoLista
            carregando={eventos.carregando}
            erro={eventos.erro}
            vazia={eventosVisiveis.length === 0}
            mensagemVazia="Nenhum evento próximo no momento."
            aoTentarNovamente={carregarEventos}
          >
            <div className="grid gap-5 md:grid-cols-2">{eventosVisiveis.map((evento) => <CartaoEvento key={evento.id} evento={evento} />)}</div>
          </EstadoLista>
        ) : (
          <EstadoLista
            carregando={temporadas.carregando}
            erro={temporadas.erro}
            vazia={temporadasOrdenadas.length === 0}
            mensagemVazia="Nenhuma temporada disponível no momento."
            aoTentarNovamente={carregarTemporadas}
          >
            <div className="grid gap-5 md:grid-cols-2">{temporadasOrdenadas.map((temporada) => <CartaoTemporada key={temporada.id} temporada={temporada} />)}</div>
          </EstadoLista>
        )}
      </div>
    </section>
  )
}

export default Agenda
