import { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import { CircleOff, LoaderCircle, ShieldCheck } from 'lucide-react'
import { atualizarCategoriaAtrativo, cadastrarCategoriaAtrativo, desativarCategoriaAtrativo, listarCategoriasAtrativosAdministracao } from '../../../servicos/categoriaAtrativoService'
import { listarAtrativos } from '../../../servicos/atrativoService'
import FormularioCategoria from './FormularioCategoria'
import DesativarCategoria from './DesativarCategoria'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'
const botao = `min-h-12 rounded-full border border-floresta px-5 py-3 font-semibold text-floresta hover:bg-creme ${foco}`
const campo = `min-h-12 w-full rounded-xl border border-floresta/30 bg-white px-3 py-3 ${foco}`
const normalizar = (valor) => (valor ?? '').trim().toLocaleLowerCase('pt-BR')

export default function GerenciarCategorias() {
  const [categorias, setCategorias] = useState([])
  const [estado, setEstado] = useState('carregando')
  const [erro, setErro] = useState('')
  const [tentativa, setTentativa] = useState(0)
  const [vinculos, setVinculos] = useState({ estado: 'carregando', atrativos: [] })
  const [tentativaVinculos, setTentativaVinculos] = useState(0)
  const [busca, setBusca] = useState('')
  const [situacao, setSituacao] = useState('todas')
  const [acao, setAcao] = useState(null)
  const [mensagem, setMensagem] = useState('')
  const titulo = useRef(null)

  useEffect(() => {
    let ativo = true
    const inicio = setTimeout(async () => {
      try {
        const dados = await listarCategoriasAtrativosAdministracao()
        if (ativo) { setCategorias(dados); setEstado('pronto') }
      } catch (falha) {
        if (ativo) {
          setErro(falha.response?.status === 403 ? 'Sua conta não possui permissão para acessar as categorias.' : 'Não foi possível carregar as categorias. Tente novamente.')
          setEstado('erro')
        }
      }
    }, 0)
    return () => { ativo = false; clearTimeout(inicio) }
  }, [tentativa])

  useEffect(() => {
    let ativo = true
    const inicio = setTimeout(async () => {
      try {
        const atrativos = await listarAtrativos()
        if (ativo) setVinculos({ estado: 'pronto', atrativos })
      } catch {
        if (ativo) setVinculos({ estado: 'erro', atrativos: [] })
      }
    }, 0)
    return () => { ativo = false; clearTimeout(inicio) }
  }, [tentativaVinculos])

  function carregarVinculos() {
    setVinculos({ estado: 'carregando', atrativos: [] })
    setTentativaVinculos((valor) => valor + 1)
  }
  function recarregar() {
    setEstado('carregando')
    setTentativa((valor) => valor + 1)
    carregarVinculos()
  }
  function quantidade(id, atrativos = vinculos.atrativos) {
    return atrativos.filter((atrativo) => atrativo.categoria?.id === id && atrativo.ativo === true).length
  }
  function podeDesativar(categoria) {
    return estado === 'pronto' && categoria?.ativo === true && vinculos.estado === 'pronto' && quantidade(categoria.id) === 0
  }
  function abrir(tipo, categoria, origem) { setMensagem(''); setAcao({ tipo, categoria, origem }) }
  function concluir(texto) { setAcao(null); setMensagem(texto); recarregar() }
  async function salvar(payload) {
    if (acao.categoria) await atualizarCategoriaAtrativo(acao.categoria.id, payload)
    else await cadastrarCategoriaAtrativo(payload)
    concluir(acao.categoria ? 'Categoria atualizada com sucesso.' : 'Categoria cadastrada com sucesso.')
  }
  async function desativar(id) {
    if (!podeDesativar(categorias.find((item) => item.id === id))) throw Object.assign(new Error(), { motivo: 'vinculos' })
    // Revalida a consulta antes do DELETE; a proteção continua sendo da interface.
    let atuais
    try { atuais = await listarAtrativos() } catch (falha) {
      setVinculos({ estado: 'erro', atrativos: [] })
      if ([401, 403].includes(falha.response?.status)) throw falha
      throw Object.assign(new Error(), { motivo: 'vinculos' })
    }
    setVinculos({ estado: 'pronto', atrativos: atuais })
    if (quantidade(id, atuais) > 0) throw Object.assign(new Error(), { motivo: 'emUso' })
    await desativarCategoriaAtrativo(id)
    concluir('Categoria desativada com sucesso.')
  }

  const ativas = categorias.filter((item) => item.ativo === true).length
  const termo = normalizar(busca)
  const filtradas = categorias.filter((item) => (situacao === 'todas' || (situacao === 'ativas' ? item.ativo === true : item.ativo === false))
    && (!termo || [item.nome, item.descricao].some((valor) => normalizar(valor).includes(termo))))
    .sort((a, b) => Number(b.ativo === true) - Number(a.ativo === true) || a.nome.localeCompare(b.nome, 'pt-BR') || a.id - b.id)

  return <section className="space-y-6 px-4 py-6 sm:px-6 sm:py-8" aria-labelledby="titulo-categorias">
    <Link to="/admin" className={`inline-flex min-h-12 items-center rounded-lg font-semibold text-floresta ${foco}`}>← Voltar ao Painel Administrativo</Link>
    <div className="rounded-2xl border border-floresta/10 bg-white p-5 sm:p-7">
      <p className="mb-3 flex items-center gap-2 font-semibold text-folha"><ShieldCheck aria-hidden="true" className="size-5" />Área administrativa</p>
      <h1 ref={titulo} tabIndex={-1} id="titulo-categorias" className={`text-2xl font-bold text-floresta sm:text-3xl ${foco}`}>Gerenciar categorias</h1>
      {estado === 'pronto' && <p className="mt-3 text-slate-600">Total: {categorias.length} · Ativas: {ativas} · Inativas: {categorias.length - ativas}</p>}
      <button type="button" onClick={(evento) => abrir('formulario', null, evento.currentTarget)} className={`mt-4 ${botao}`}>Nova categoria</button>
    </div>
    {mensagem && <p role="status" className="rounded-xl bg-white p-4 font-semibold text-floresta">{mensagem}</p>}
    {vinculos.estado === 'carregando' && <p role="status" className="text-sm text-slate-600">Verificando atrativos vinculados. As desativações ficam bloqueadas durante a consulta.</p>}
    {vinculos.estado === 'erro' && <div className="rounded-xl border border-floresta/20 bg-white p-4"><p role="alert" className="text-sm">Não foi possível verificar os atrativos vinculados. As desativações estão bloqueadas.</p><button type="button" onClick={carregarVinculos} className={`mt-3 ${botao}`}>Tentar carregar vínculos novamente</button></div>}
    {estado === 'carregando' && <p role="status" className="flex items-center gap-3 rounded-xl bg-white p-5 text-floresta"><LoaderCircle aria-hidden="true" className="size-5 animate-spin motion-reduce:animate-none" />Carregando categorias…</p>}
    {estado === 'erro' && <div className="rounded-xl bg-white p-5"><p role="alert">{erro}</p><button type="button" onClick={recarregar} className={`mt-4 ${botao}`}>Tentar novamente</button></div>}
    {estado === 'pronto' && <>
      <div className="space-y-4 rounded-2xl border border-floresta/10 bg-white p-5">
        <div className="grid gap-4 sm:grid-cols-2">
          <div><label htmlFor="busca-categorias" className="mb-2 block font-semibold text-floresta">Buscar por nome ou descrição</label><input id="busca-categorias" type="search" value={busca} onChange={(evento) => setBusca(evento.target.value)} className={campo} /></div>
          <div><label htmlFor="situacao-categorias" className="mb-2 block font-semibold text-floresta">Situação</label><select id="situacao-categorias" value={situacao} onChange={(evento) => setSituacao(evento.target.value)} className={campo}><option value="todas">Todas</option><option value="ativas">Ativas</option><option value="inativas">Inativas</option></select></div>
        </div>
        <div className="flex flex-wrap items-center justify-between gap-3"><p role="status">Exibindo {filtradas.length} de {categorias.length} categorias</p><button type="button" onClick={() => { setBusca(''); setSituacao('todas') }} className={botao}>Limpar busca e filtros</button></div>
      </div>
      {categorias.length === 0 ? <p role="status" className="rounded-xl bg-white p-5">Nenhuma categoria cadastrada.</p> : filtradas.length === 0 ? <p role="status" className="rounded-xl bg-white p-5">Nenhuma categoria encontrada. Altere a busca ou limpe os filtros.</p> : <ul aria-label="Categorias cadastradas" className="grid gap-5 md:grid-cols-2">
        {filtradas.map((categoria) => <li key={categoria.id} className="flex min-w-0 flex-col rounded-2xl border border-floresta/10 bg-white p-5 shadow-sm">
          <h2 className="text-xl font-bold text-floresta [overflow-wrap:anywhere]">{categoria.nome}</h2>
          <p className="mt-3 self-start rounded-full bg-creme px-3 py-1 font-semibold text-floresta">{categoria.ativo ? 'Ativa' : 'Inativa'}</p>
          {categoria.descricao && <p className="mt-3 whitespace-pre-line text-slate-600 [overflow-wrap:anywhere]">{categoria.descricao}</p>}
          <p className="mt-3 text-sm">Atrativos ativos vinculados: {vinculos.estado === 'pronto' ? quantidade(categoria.id) : 'não verificado'}</p>
          {vinculos.estado === 'pronto' && quantidade(categoria.id) > 0 && <p id={`vinculos-${categoria.id}`} className="mt-3 text-sm text-slate-600">Esta categoria está sendo usada por {quantidade(categoria.id)} atrativo(s) ativo(s). Mova os atrativos para outra categoria ou desative-os antes.</p>}
          <div className="mt-auto grid gap-3 pt-5 sm:grid-cols-2">
            <button type="button" aria-label={`Editar categoria: ${categoria.nome}`} onClick={(evento) => abrir('formulario', categoria, evento.currentTarget)} className={botao}>Editar</button>
            {categoria.ativo === true && <button type="button" disabled={!podeDesativar(categoria)} aria-label={`Desativar categoria: ${categoria.nome}`} aria-describedby={quantidade(categoria.id) > 0 ? `vinculos-${categoria.id}` : undefined} onClick={(evento) => abrir('desativar', categoria, evento.currentTarget)} className={`inline-flex min-h-12 items-center justify-center gap-2 rounded-full border border-red-800 px-5 py-3 font-semibold text-red-800 hover:bg-red-50 disabled:border-slate-300 disabled:bg-slate-100 disabled:text-slate-600 ${foco}`}><CircleOff aria-hidden="true" className="size-5 shrink-0" />Desativar</button>}
          </div>
        </li>)}
      </ul>}
    </>}
    {acao?.tipo === 'formulario' && <FormularioCategoria categoria={acao.categoria} origem={acao.origem} focoAlternativo={titulo} onCancelar={() => setAcao(null)} onSalvar={salvar} onSincronizar={recarregar} />}
    {acao?.tipo === 'desativar' && <DesativarCategoria categoria={acao.categoria} origem={acao.origem} focoAlternativo={titulo} permitido={podeDesativar(categorias.find((item) => item.id === acao.categoria.id))} onCancelar={() => setAcao(null)} onDesativar={desativar} onSincronizar={recarregar} />}
  </section>
}
