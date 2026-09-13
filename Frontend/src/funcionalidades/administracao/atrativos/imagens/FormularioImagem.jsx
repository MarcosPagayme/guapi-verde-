import { useEffect, useRef, useState } from 'react'
import DialogoImagem from './DialogoImagem'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'
const campo = `min-h-12 w-full rounded-xl border border-floresta/30 bg-white px-3 py-3 disabled:bg-slate-100 ${foco}`

export default function FormularioImagem({ imagem, atrativoId, origem, focoAlternativo, onCancelar, onSalvar, onSincronizar }) {
  const [valores, setValores] = useState({ url: imagem?.url ?? '', textoAlternativo: imagem?.textoAlternativo ?? '', principal: imagem?.principal ?? false, ordem: String(imagem?.ordem ?? '') })
  const [erros, setErros] = useState({})
  const [erroGeral, setErroGeral] = useState('')
  const [salvando, setSalvando] = useState(false)
  const bloqueado = useRef(false)
  const focoPendente = useRef(false)
  const formulario = useRef(null)

  useEffect(() => {
    if (!salvando && focoPendente.current) {
      focoPendente.current = false
      ;[...formulario.current.elements].find((item) => erros[item.name])?.focus()
    }
  }, [erros, salvando])

  function alterar(nome, valor) {
    setValores((atuais) => ({ ...atuais, [nome]: valor }))
    setErros((atuais) => ({ ...atuais, [nome]: undefined }))
    setErroGeral('')
  }

  function fechar() { if (!bloqueado.current) onCancelar() }

  async function enviar(evento) {
    evento.preventDefault()
    if (bloqueado.current) return
    const novos = {}
    try {
      const url = new URL(valores.url.trim())
      if (!/^https?:\/\//i.test(valores.url.trim()) || !['http:', 'https:'].includes(url.protocol) || !url.hostname || /\s/.test(valores.url.trim())) throw new Error()
    } catch { novos.url = 'Informe uma URL HTTP ou HTTPS válida.' }
    if (!valores.url.trim()) novos.url = 'A URL da imagem é obrigatória.'
    if (valores.url.length > 500) novos.url = 'Use no máximo 500 caracteres.'
    if (!valores.textoAlternativo.trim()) novos.textoAlternativo = 'O texto alternativo é obrigatório.'
    if (valores.textoAlternativo.length > 180) novos.textoAlternativo = 'Use no máximo 180 caracteres.'
    if (!/^\d+$/.test(valores.ordem.trim()) || Number(valores.ordem) > 2147483647) novos.ordem = 'Informe um número inteiro entre 0 e 2147483647.'
    if (!valores.ordem.trim()) novos.ordem = 'A ordem é obrigatória.'
    if (Object.keys(novos).length) { focoPendente.current = true; setErros(novos); return }
    bloqueado.current = true
    setSalvando(true)
    setErroGeral('')
    setErros({})
    try {
      await onSalvar({ atrativoId: Number(atrativoId), url: valores.url.trim(), textoAlternativo: valores.textoAlternativo.trim(), principal: valores.principal, ordem: Number(valores.ordem) })
    } catch (falha) {
      const status = falha.response?.status
      const campos = falha.response?.data?.campos
      if (status === 400 && campos && typeof campos === 'object') {
        focoPendente.current = true
        setErros(Object.fromEntries(Object.entries(campos).filter(([nome, mensagem]) => Object.hasOwn(valores, nome) && typeof mensagem === 'string')))
      }
      setErroGeral(status === 403 ? 'Sua conta não possui permissão para gerenciar imagens.'
        : status === 401 ? 'Sua sessão expirou. Entre novamente para continuar.'
          : status === 404 ? 'A imagem ou o atrativo não está mais disponível. A página será sincronizada; feche o formulário para conferir.'
            : 'Não foi possível salvar a imagem. Confira os campos e tente novamente.')
      if (status === 404) onSincronizar()
    } finally { bloqueado.current = false; setSalvando(false) }
  }

  function atributos(nome) {
    return { id: `imagem-${nome}`, name: nome, value: valores[nome], onChange: (evento) => alterar(nome, evento.target.value), required: true, 'aria-invalid': Boolean(erros[nome]), 'aria-describedby': `${nome}-ajuda${erros[nome] ? ` ${nome}-erro` : ''}`, className: campo }
  }

  return <DialogoImagem titulo={imagem ? 'Editar imagem' : 'Adicionar imagem'} origem={origem} focoAlternativo={focoAlternativo} onCancelar={fechar}>
    <form ref={formulario} onSubmit={enviar} noValidate className="space-y-5" aria-busy={salvando}>
      <fieldset disabled={salvando} className="min-w-0 space-y-5">
        <div>
          <label htmlFor="imagem-url" className="mb-2 block font-semibold text-floresta">URL da imagem</label>
          <input {...atributos('url')} data-foco-inicial type="url" maxLength={500} placeholder="https://…" />
          <p id="url-ajuda" className="mt-1 text-sm">Endereço HTTP ou HTTPS, com até 500 caracteres.</p>
          {erros.url && <p id="url-erro" role="alert" className="mt-2 text-red-800">{erros.url}</p>}
        </div>
        <div>
          <label htmlFor="imagem-textoAlternativo" className="mb-2 block font-semibold text-floresta">Texto alternativo</label>
          <textarea {...atributos('textoAlternativo')} maxLength={180} rows={3} />
          <p id="textoAlternativo-ajuda" className="mt-1 text-sm">{valores.textoAlternativo.length}/180 caracteres. Descreva a imagem para pessoas que não conseguem visualizá-la.</p>
          {erros.textoAlternativo && <p id="textoAlternativo-erro" role="alert" className="mt-2 text-red-800">{erros.textoAlternativo}</p>}
        </div>
        <div>
          <label htmlFor="imagem-ordem" className="mb-2 block font-semibold text-floresta">Ordem</label>
          <input {...atributos('ordem')} inputMode="numeric" />
          <p id="ordem-ajuda" className="mt-1 text-sm">Número inteiro a partir de zero. Os menores aparecem primeiro.</p>
          {erros.ordem && <p id="ordem-erro" role="alert" className="mt-2 text-red-800">{erros.ordem}</p>}
        </div>
        <div>
          <label htmlFor="imagem-principal" className="flex min-h-12 items-center gap-3 font-semibold text-floresta"><input id="imagem-principal" name="principal" type="checkbox" checked={valores.principal} onChange={(evento) => alterar('principal', evento.target.checked)} aria-invalid={Boolean(erros.principal)} aria-describedby={`principal-ajuda${erros.principal ? ' principal-erro' : ''}`} className={`size-5 shrink-0 accent-floresta ${foco}`} />Imagem principal</label>
          <p id="principal-ajuda" className="mt-1 text-sm">Somente uma imagem pode ser principal. Ao marcar esta, as outras serão desmarcadas.</p>
          {erros.principal && <p id="principal-erro" role="alert" className="mt-2 text-red-800">{erros.principal}</p>}
        </div>
      </fieldset>
      {erroGeral && <p role="alert" className="rounded-xl bg-red-50 p-3 text-red-800">{erroGeral}</p>}
      <div className="flex flex-col gap-3 sm:flex-row sm:justify-end">
        <button type="button" disabled={salvando} onClick={fechar} className={`min-h-12 rounded-full border border-floresta px-5 py-3 font-semibold text-floresta ${foco}`}>Cancelar</button>
        <button type="submit" disabled={salvando} className={`min-h-12 rounded-full bg-floresta px-5 py-3 font-semibold text-white disabled:bg-slate-600 ${foco}`}>{salvando ? 'Salvando…' : imagem ? 'Salvar alterações' : 'Adicionar imagem'}</button>
      </div>
      <p role="status" className="sr-only">{salvando ? 'Salvando imagem. Aguarde.' : ''}</p>
    </form>
  </DialogoImagem>
}
