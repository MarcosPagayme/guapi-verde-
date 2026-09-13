import { useEffect, useRef, useState } from 'react'
import { Accessibility, ArrowLeft, ArrowRight, CircleAlert, CircleOff, LoaderCircle, MapPin, Search, ShieldCheck, Ticket, X } from 'lucide-react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { desativarAtrativo, listarAtrativos } from '../../../servicos/atrativoService'
import ConfirmarDesativacao from './componentes/ConfirmarDesativacao'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'
const campo = `min-h-12 w-full rounded-xl border border-floresta/20 bg-white px-3 text-slate-700 ${foco}`
const moeda = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

function normalizar(texto) {
  return (texto ?? '').toLocaleLowerCase('pt-BR').trim().replace(/\s+/g, ' ')
}

function formatarSituacao(situacao) {
  if (!situacao) return 'Situação não informada'
  if (situacao === 'INDISPONIVEL') return 'Indisponível'
  const texto = situacao.toLocaleLowerCase('pt-BR').replace(/_/g, ' ')
  return texto.charAt(0).toLocaleUpperCase('pt-BR') + texto.slice(1)
}

function formatarEntrada(atrativo) {
  if (atrativo.gratuito === true) return 'Gratuito'
  if (atrativo.valorEntrada != null) return moeda.format(atrativo.valorEntrada)
  return atrativo.gratuito === false ? 'Pago · valor não informado' : 'Entrada não informada'
}

function GerenciarAtrativos() {
  const location = useLocation()
  const navigate = useNavigate()
  const [sucesso, setSucesso] = useState(() => location.state?.sucesso)
  useEffect(() => {
    if (location.state?.sucesso) navigate(location.pathname, { replace: true, state: null })
  }, [location.state, location.pathname, navigate])
  const [atrativos, setAtrativos] = useState([])
  const [estado, setEstado] = useState('carregando')
  const [tentativa, setTentativa] = useState(0)
  const [busca, setBusca] = useState('')
  const [categoria, setCategoria] = useState('')
  const [situacao, setSituacao] = useState('')
  const [selecionado, setSelecionado] = useState(null)
  const titulo = useRef(null)
  const botaoOrigem = useRef(null)

  useEffect(() => {
    let ativo = true
    async function carregar() {
      try {
        const dados = await listarAtrativos()
        if (ativo) {
          setAtrativos(dados)
          setEstado('pronto')
        }
      } catch {
        if (ativo) setEstado('erro')
      }
    }
    const inicio = setTimeout(carregar, 0)
    return () => {
      ativo = false
      clearTimeout(inicio)
    }
  }, [tentativa])

  const categorias = [...new Map(atrativos.filter((item) => item.categoria?.id != null)
    .map((item) => [String(item.categoria.id), item.categoria])).values()]
    .sort((a, b) => (a.nome ?? '').localeCompare(b.nome ?? '', 'pt-BR'))
  const situacoes = [...new Set(atrativos.map((item) => item.situacao).filter(Boolean))]
    .sort((a, b) => formatarSituacao(a).localeCompare(formatarSituacao(b), 'pt-BR'))
  const termo = normalizar(busca)
  const filtrados = atrativos.filter((item) => (
    (!categoria || String(item.categoria?.id) === categoria)
    && (!situacao || item.situacao === situacao)
    && (!termo || [item.nome, item.resumo, item.categoria?.nome, item.endereco]
      .some((texto) => normalizar(texto).includes(termo)))
  ))

  function limparFiltros() {
    setBusca('')
    setCategoria('')
    setSituacao('')
  }

  async function confirmarDesativacao(id) {
    await desativarAtrativo(id)
    setAtrativos((atuais) => atuais.filter((item) => item.id !== id))
    setSucesso('Atrativo desativado com sucesso.')
    setSelecionado(null)
  }

  function sincronizarListagem() {
    setEstado('carregando')
    setTentativa((valor) => valor + 1)
  }

  return (
    <div className="space-y-6 px-4 py-6 sm:px-6 sm:py-8">
      <Link to="/admin" className={`inline-flex min-h-12 items-center gap-2 rounded-lg font-semibold text-floresta ${foco}`}>
        <ArrowLeft aria-hidden="true" className="size-5" /> Voltar ao Painel Administrativo
      </Link>
      <section aria-labelledby="titulo-gerenciar" className="rounded-2xl border border-floresta/10 bg-white p-6 shadow-sm sm:p-8">
        <span className="inline-flex items-center gap-2 rounded-full bg-folha/10 px-3 py-1 text-sm font-semibold text-floresta">
          <ShieldCheck aria-hidden="true" className="size-4" /> Área administrativa
        </span>
        <h1 ref={titulo} tabIndex={-1} id="titulo-gerenciar" className={`mt-4 text-2xl font-bold text-floresta sm:text-3xl ${foco}`}>Gerenciar atrativos</h1>
        <Link to="/admin/atrativos/novo" className={`mt-4 inline-flex min-h-12 items-center rounded-full bg-floresta px-6 font-semibold text-white hover:bg-folha ${foco}`}>Novo atrativo</Link>
        <p className="mt-3 text-slate-600">Consulte os atrativos ativos e organize a visualização usando a busca e os filtros.</p>
        {estado === 'pronto' && <p className="mt-4 font-semibold text-floresta">Atrativos ativos: {atrativos.length}</p>}
      </section>

      {sucesso && <p role="status" className="rounded-2xl border border-folha/30 bg-white p-5 font-semibold text-floresta">{sucesso}</p>}

      {estado === 'carregando' && <p role="status" className="flex items-center gap-3 rounded-2xl bg-white p-6 text-floresta"><LoaderCircle aria-hidden="true" className="size-5 animate-spin motion-reduce:animate-none" /> Carregando atrativos…</p>}
      {estado === 'erro' && (
        <div className="rounded-2xl border border-floresta/10 bg-white p-6">
          <p role="alert" className="flex items-center gap-3 text-slate-700"><CircleAlert aria-hidden="true" className="size-5 shrink-0" /> Não foi possível carregar os atrativos. Tente novamente.</p>
          <button type="button" onClick={() => { setEstado('carregando'); setTentativa((valor) => valor + 1) }} className={`mt-4 min-h-12 rounded-full bg-floresta px-6 font-semibold text-white hover:bg-folha ${foco}`}>Tentar novamente</button>
        </div>
      )}
      {estado === 'pronto' && (atrativos.length === 0 ? (
        <p role="status" className="rounded-2xl bg-white p-6 text-slate-600">Nenhum atrativo ativo cadastrado no momento.</p>
      ) : (
        <>
          <section aria-label="Busca e filtros" className="space-y-4 rounded-2xl border border-floresta/10 bg-white p-5 sm:p-6">
            <div>
              <label htmlFor="busca-atrativos" className="mb-2 block font-semibold text-floresta">Buscar atrativos</label>
              <div className="relative">
                <Search aria-hidden="true" className="pointer-events-none absolute top-4 left-3 size-5 text-slate-500" />
                <input id="busca-atrativos" type="search" value={busca} onChange={(evento) => setBusca(evento.target.value)} placeholder="Nome, resumo, categoria ou endereço" className={`${campo} pr-14 pl-10`} />
                {busca && <button type="button" aria-label="Limpar busca" onClick={() => setBusca('')} className={`absolute top-0 right-0 flex min-h-12 min-w-12 items-center justify-center rounded-xl text-floresta ${foco}`}><X aria-hidden="true" className="size-5" /></button>}
              </div>
            </div>
            <div className="grid gap-4 sm:grid-cols-2">
              <div>
                <label htmlFor="categoria-atrativos" className="mb-2 block font-semibold text-floresta">Categoria</label>
                <select id="categoria-atrativos" value={categoria} onChange={(evento) => setCategoria(evento.target.value)} className={campo}>
                  <option value="">Todas as categorias</option>
                  {categoria && !categorias.some((item) => String(item.id) === categoria) && <option value={categoria}>Categoria selecionada (sem atrativos ativos)</option>}
                  {categorias.map((item) => <option key={item.id} value={item.id}>{item.nome ?? 'Categoria sem nome informado'}</option>)}
                </select>
              </div>
              <div>
                <label htmlFor="situacao-atrativos" className="mb-2 block font-semibold text-floresta">Situação</label>
                <select id="situacao-atrativos" value={situacao} onChange={(evento) => setSituacao(evento.target.value)} className={campo}>
                  <option value="">Todas as situações</option>
                  {situacao && !situacoes.includes(situacao) && <option value={situacao}>{formatarSituacao(situacao)}</option>}
                  {situacoes.map((item) => <option key={item} value={item}>{formatarSituacao(item)}</option>)}
                </select>
              </div>
            </div>
            <div className="flex flex-wrap items-center justify-between gap-3">
              <p role="status" className="text-sm text-slate-600">Exibindo {filtrados.length} de {atrativos.length} atrativos ativos</p>
              <button type="button" onClick={limparFiltros} className={`min-h-12 rounded-lg px-3 font-semibold text-floresta underline underline-offset-4 hover:bg-creme ${foco}`}>Limpar busca e filtros</button>
            </div>
          </section>
          {filtrados.length === 0 ? (
            <p role="status" className="rounded-2xl bg-white p-6 text-slate-600">Nenhum atrativo encontrado. Altere a busca ou limpe os filtros para visualizar os atrativos ativos.</p>
          ) : (
            <ul aria-label="Atrativos ativos" className="grid gap-4 lg:grid-cols-2">
              {filtrados.map((atrativo) => (
                <li key={atrativo.id} className="flex min-w-0 flex-col rounded-2xl border border-floresta/10 bg-white p-5 shadow-sm [overflow-wrap:anywhere] sm:p-6">
                  <div className="flex items-start gap-3">
                    <span className="rounded-xl bg-folha/10 p-3 text-folha"><MapPin aria-hidden="true" className="size-6" /></span>
                    <div className="min-w-0">
                      <p className="text-sm font-semibold text-folha">{atrativo.categoria?.nome ?? 'Categoria não informada'}</p>
                      <h2 className="mt-1 text-xl font-bold text-floresta">{atrativo.nome ?? 'Nome não informado'}</h2>
                    </div>
                  </div>
                  <p className="mt-4 leading-relaxed text-slate-600">{atrativo.resumo ?? 'Resumo não informado'}</p>
                  <p className="mt-4 self-start rounded-full bg-creme px-3 py-1 text-sm font-semibold text-floresta">{formatarSituacao(atrativo.situacao)}</p>
                  <div className="my-4 space-y-3 text-sm text-slate-600">
                    <p className="flex items-center gap-2"><Ticket aria-hidden="true" className="size-5 shrink-0" />{formatarEntrada(atrativo)}</p>
                    <p className="flex items-center gap-2"><Accessibility aria-hidden="true" className="size-5 shrink-0" />{atrativo.acessivel === true ? 'Acessível' : atrativo.acessivel === false ? 'Não acessível' : 'Acessibilidade não informada'}</p>
                    {atrativo.endereco && <p className="flex items-start gap-2"><MapPin aria-hidden="true" className="size-5 shrink-0" />{atrativo.endereco}</p>}
                  </div>
                  <Link to={`/atrativos/${atrativo.id}`} aria-label={`Ver no aplicativo: ${atrativo.nome ?? 'atrativo'}`} className={`mt-auto inline-flex min-h-12 items-center justify-center gap-2 rounded-full border border-floresta px-4 py-2 font-semibold text-floresta hover:bg-creme ${foco}`}>Ver no aplicativo <ArrowRight aria-hidden="true" className="size-4" /></Link>
                  <Link to={`/admin/atrativos/${atrativo.id}/editar`} aria-label={`Editar: ${atrativo.nome}`} className={`mt-3 inline-flex min-h-12 items-center justify-center rounded-full bg-floresta px-4 py-2 font-semibold text-white hover:bg-folha ${foco}`}>Editar</Link>
                  <Link to={`/admin/atrativos/${atrativo.id}/imagens`} aria-label={`Gerenciar imagens: ${atrativo.nome}`} className={`mt-3 inline-flex min-h-12 items-center justify-center rounded-full border border-floresta px-4 py-2 font-semibold text-floresta hover:bg-creme ${foco}`}>Gerenciar imagens</Link>
                  <button type="button" aria-label={`Desativar: ${atrativo.nome}`} disabled={Boolean(selecionado)} onClick={(evento) => { botaoOrigem.current = evento.currentTarget; setSucesso(''); setSelecionado(atrativo) }} className={`mt-3 inline-flex min-h-12 items-center justify-center gap-2 rounded-full border border-red-800 px-4 py-2 font-semibold text-red-800 hover:bg-red-50 disabled:text-slate-600 ${foco}`}><CircleOff aria-hidden="true" className="size-5 shrink-0" />Desativar</button>
                </li>
              ))}
            </ul>
          )}
        </>
      ))}
      {selecionado && <ConfirmarDesativacao key={selecionado.id} atrativo={selecionado} onConfirmar={confirmarDesativacao} onCancelar={() => setSelecionado(null)} onIndisponivel={sincronizarListagem} focoOrigem={botaoOrigem} focoAlternativo={titulo} />}
    </div>
  )
}

export default GerenciarAtrativos
