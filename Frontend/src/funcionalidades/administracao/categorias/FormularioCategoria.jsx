import { useEffect, useRef, useState } from 'react'
import DialogoCategoria from './DialogoCategoria'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'
const campo = `min-h-12 w-full rounded-xl border border-floresta/30 bg-white px-3 py-3 disabled:bg-slate-100 ${foco}`

export default function FormularioCategoria({ categoria, origem, focoAlternativo, onCancelar, onSalvar, onSincronizar }) {
  const [nome, setNome] = useState(categoria?.nome ?? '')
  const [descricao, setDescricao] = useState(categoria?.descricao ?? '')
  const [erros, setErros] = useState({})
  const [erro, setErro] = useState('')
  const [salvando, setSalvando] = useState(false)
  const bloqueado = useRef(false)
  const focoPendente = useRef(false)
  const formulario = useRef(null)
  useEffect(() => {
    if (!salvando && focoPendente.current) {
      focoPendente.current = false
      const primeiro = [...formulario.current.elements].find((elemento) => erros[elemento.name])
      primeiro?.focus()
    }
  }, [erros, salvando])
  function fechar() { if (!bloqueado.current) onCancelar() }
  async function salvar(evento) {
    evento.preventDefault()
    if (bloqueado.current) return
    const novos = {}
    if (!nome.trim()) novos.nome = 'O nome é obrigatório.'
    if (nome.length > 80) novos.nome = 'Use no máximo 80 caracteres.'
    if (descricao.length > 255) novos.descricao = 'Use no máximo 255 caracteres.'
    if (Object.keys(novos).length) { focoPendente.current = true; setErros(novos); return }
    bloqueado.current = true
    setSalvando(true)
    setErros({})
    setErro('')
    try { await onSalvar({ nome: nome.trim(), descricao: descricao.trim() || null }) } catch (falha) {
      const status = falha.response?.status
      const campos = falha.response?.data?.campos
      if (status === 409) {
        focoPendente.current = true
        setErros({ nome: 'Já existe uma categoria cadastrada com esse nome.' })
      } else {
        if (status === 400 && campos && typeof campos === 'object') {
          focoPendente.current = true
          setErros(Object.fromEntries(Object.entries(campos).filter(([chave, mensagem]) => ['nome', 'descricao'].includes(chave) && typeof mensagem === 'string')))
        }
        setErro(status === 403 ? 'Sua conta não possui permissão para gerenciar categorias.'
          : status === 401 ? 'Sua sessão expirou. Entre novamente para continuar.'
            : status === 404 ? 'Categoria não encontrada. A lista será atualizada; feche o formulário para conferir.'
              : 'Não foi possível salvar a categoria. Confira os campos e tente novamente.')
        if (status === 404) onSincronizar()
      }
    } finally { bloqueado.current = false; setSalvando(false) }
  }
  return <DialogoCategoria titulo={categoria ? 'Editar categoria' : 'Nova categoria'} origem={origem} focoAlternativo={focoAlternativo} onCancelar={fechar}>
    {categoria?.ativo === false && <p className="mb-4 rounded-xl bg-creme p-3">Esta categoria está inativa. Editar seus dados não a reativa.</p>}
    <form ref={formulario} onSubmit={salvar} noValidate aria-busy={salvando} className="space-y-5">
      <fieldset disabled={salvando} className="min-w-0 space-y-5">
        <div>
          <label htmlFor="categoria-nome" className="mb-2 block font-semibold text-floresta">Nome</label>
          <input data-foco-inicial id="categoria-nome" name="nome" required maxLength={80} value={nome} onChange={(evento) => { setNome(evento.target.value); setErros((atuais) => ({ ...atuais, nome: undefined })); setErro('') }} aria-invalid={Boolean(erros.nome)} aria-describedby={erros.nome ? 'categoria-nome-erro' : undefined} className={campo} />
          {erros.nome && <p id="categoria-nome-erro" role="alert" className="mt-2 text-red-800">{erros.nome}</p>}
        </div>
        <div>
          <label htmlFor="categoria-descricao" className="mb-2 block font-semibold text-floresta">Descrição (opcional)</label>
          <textarea id="categoria-descricao" name="descricao" maxLength={255} rows={4} value={descricao} onChange={(evento) => { setDescricao(evento.target.value); setErros((atuais) => ({ ...atuais, descricao: undefined })); setErro('') }} aria-invalid={Boolean(erros.descricao)} aria-describedby={`categoria-contador${erros.descricao ? ' categoria-descricao-erro' : ''}`} className={campo} />
          <p id="categoria-contador" className="mt-1 text-sm">{descricao.length}/255 caracteres</p>
          {erros.descricao && <p id="categoria-descricao-erro" role="alert" className="mt-2 text-red-800">{erros.descricao}</p>}
        </div>
      </fieldset>
      {erro && <p role="alert" className="rounded-xl bg-red-50 p-3 text-red-800">{erro}</p>}
      <div className="flex flex-col gap-3 sm:flex-row sm:justify-end">
        <button type="button" onClick={fechar} disabled={salvando} className={`min-h-12 rounded-full border border-floresta px-5 py-3 font-semibold text-floresta ${foco}`}>Cancelar</button>
        <button type="submit" disabled={salvando} className={`min-h-12 rounded-full bg-floresta px-5 py-3 font-semibold text-white disabled:bg-slate-600 ${foco}`}>{salvando ? 'Salvando…' : 'Salvar'}</button>
      </div>
      <p role="status" className="sr-only">{salvando ? 'Salvando categoria. Aguarde.' : ''}</p>
    </form>
  </DialogoCategoria>
}
