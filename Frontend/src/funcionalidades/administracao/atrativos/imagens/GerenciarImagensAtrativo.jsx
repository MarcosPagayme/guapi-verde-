import { useEffect, useRef, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { LoaderCircle } from 'lucide-react'
import { obterAtrativoPorId } from '../../../../servicos/atrativoService'
import { atualizarImagemAtrativo, cadastrarImagemAtrativo, excluirImagemAtrativo, listarImagensDoAtrativo } from '../../../../servicos/imagemAtrativoService'
import PreviewImagem from './PreviewImagem'
import FormularioImagem from './FormularioImagem'
import ExcluirImagem from './ExcluirImagem'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'
const botao = `inline-flex min-h-12 items-center justify-center rounded-full border border-floresta px-5 py-3 font-semibold text-floresta hover:bg-creme ${foco}`

export default function GerenciarImagensAtrativo() {
  const { atrativoId } = useParams()
  return <PaginaImagens key={atrativoId} atrativoId={atrativoId} />
}

function PaginaImagens({ atrativoId }) {
  const [dados, setDados] = useState(null)
  const [estado, setEstado] = useState('carregando')
  const [erro, setErro] = useState('')
  const [tentativa, setTentativa] = useState(0)
  const [acao, setAcao] = useState(null)
  const [mensagem, setMensagem] = useState('')
  const titulo = useRef(null)

  useEffect(() => {
    let ativo = true
    async function carregar() {
      try {
        const [atrativo, imagens] = await Promise.all([obterAtrativoPorId(atrativoId), listarImagensDoAtrativo(atrativoId)])
        if (ativo) { setDados({ atrativo, imagens: [...imagens].sort((a, b) => a.ordem - b.ordem || a.id - b.id) }); setEstado('pronto') }
      } catch (falha) {
        if (ativo) {
          setErro(falha.response?.status === 404 ? 'Atrativo não encontrado ou desativado.' : falha.response?.status === 403 ? 'Sua conta não possui permissão para acessar estas imagens.' : 'Não foi possível carregar as imagens do atrativo. Tente novamente.')
          setEstado('erro')
        }
      }
    }
    const inicio = setTimeout(carregar, 0)
    return () => { ativo = false; clearTimeout(inicio) }
  }, [atrativoId, tentativa])

  function recarregar() { setEstado('carregando'); setTentativa((valor) => valor + 1) }
  function abrir(tipo, imagem, origem) { setMensagem(''); setAcao({ tipo, imagem, origem }) }
  function concluir(texto) {
    setAcao(null)
    setMensagem(texto)
    recarregar()
  }
  async function salvar(payload) {
    if (acao.imagem) await atualizarImagemAtrativo(acao.imagem.id, payload)
    else await cadastrarImagemAtrativo(payload)
    concluir(acao.imagem ? 'Imagem atualizada com sucesso.' : 'Imagem cadastrada com sucesso.')
  }
  async function excluir(id) {
    await excluirImagemAtrativo(id)
    concluir('Imagem excluída com sucesso.')
  }

  return <section className="space-y-6 px-4 py-6 sm:px-6 sm:py-8" aria-labelledby="titulo-imagens">
    <Link to="/admin/atrativos" className={`inline-flex min-h-12 items-center rounded-lg font-semibold text-floresta ${foco}`}>← Voltar aos atrativos</Link>
    <div className="rounded-2xl border border-floresta/10 bg-white p-5 sm:p-7">
      <h1 ref={titulo} tabIndex={-1} id="titulo-imagens" className={`text-2xl font-bold text-floresta sm:text-3xl ${foco}`}>Imagens do atrativo</h1>
      {dados && <p className="mt-3 text-lg text-slate-700">{dados.atrativo.nome}</p>}
      {estado === 'pronto' && <>
        <p className="mt-3 text-slate-600">Imagens cadastradas: {dados.imagens.length}</p>
        <button type="button" onClick={(evento) => abrir('formulario', null, evento.currentTarget)} className={`mt-4 ${botao}`}>Adicionar imagem</button>
      </>}
    </div>
    {mensagem && <p role="status" className="rounded-xl bg-white p-4 font-semibold text-floresta">{mensagem}</p>}
    {estado === 'carregando' && <p role="status" className="flex items-center gap-3 rounded-xl bg-white p-5 text-floresta"><LoaderCircle aria-hidden="true" className="size-5 animate-spin motion-reduce:animate-none" />Carregando imagens…</p>}
    {estado === 'erro' && <div className="rounded-xl bg-white p-5"><p role="alert">{erro}</p><button type="button" onClick={recarregar} className={`mt-4 ${botao}`}>Tentar novamente</button></div>}
    {estado === 'pronto' && (dados.imagens.length === 0 ? <p role="status" className="rounded-xl bg-white p-5">Nenhuma imagem cadastrada para este atrativo.</p> : <ul aria-label="Imagens cadastradas" className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
      {dados.imagens.map((imagem) => <li key={imagem.id} className="flex min-w-0 flex-col rounded-2xl border border-floresta/10 bg-white p-5 shadow-sm">
        <PreviewImagem url={imagem.url} textoAlternativo={imagem.textoAlternativo} />
        <p className="mt-4 font-semibold text-floresta [overflow-wrap:anywhere]">{imagem.textoAlternativo}</p>
        <p className="mt-2 text-slate-600">Ordem: {imagem.ordem}</p>
        {imagem.principal && <p className="mt-3 self-start rounded-full bg-folha/10 px-3 py-1 font-semibold text-floresta">Imagem principal</p>}
        <div className="mt-auto grid gap-3 pt-5 sm:grid-cols-2">
          <button type="button" aria-label={`Editar imagem: ${imagem.textoAlternativo}`} onClick={(evento) => abrir('formulario', imagem, evento.currentTarget)} className={botao}>Editar</button>
          <button type="button" aria-label={`Excluir imagem: ${imagem.textoAlternativo}`} onClick={(evento) => abrir('excluir', imagem, evento.currentTarget)} className={`min-h-12 rounded-full border border-red-800 px-5 py-3 font-semibold text-red-800 hover:bg-red-50 ${foco}`}>Excluir</button>
        </div>
      </li>)}
    </ul>)}
    {acao?.tipo === 'formulario' && <FormularioImagem atrativoId={atrativoId} imagem={acao.imagem} origem={acao.origem} focoAlternativo={titulo} onCancelar={() => setAcao(null)} onSalvar={salvar} onSincronizar={recarregar} />}
    {acao?.tipo === 'excluir' && <ExcluirImagem imagem={acao.imagem} origem={acao.origem} focoAlternativo={titulo} onCancelar={() => setAcao(null)} onExcluir={excluir} onSincronizar={recarregar} />}
  </section>
}
