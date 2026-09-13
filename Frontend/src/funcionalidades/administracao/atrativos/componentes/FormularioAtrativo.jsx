import { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'
const estiloCampo = `min-h-12 w-full min-w-0 rounded-xl border border-floresta/30 bg-white px-3 py-3 text-slate-800 disabled:bg-slate-100 disabled:text-slate-600 ${foco}`
const situacoes = { ABERTO: 'Aberto', FECHADO_TEMPORARIAMENTE: 'Fechado temporariamente', INDISPONIVEL: 'Indisponível' }
const limites = { nome: 150, resumo: 300, endereco: 255, telefone: 30, site: 255 }
const camposTexto = ['nome', 'resumo', 'descricao', 'endereco', 'latitude', 'longitude', 'telefone', 'site', 'valorEntrada']

export default function FormularioAtrativo({ inicial, categorias, onSalvar, edicao }) {
  const [valores, setValores] = useState(() => ({
    ...Object.fromEntries(camposTexto.map((nome) => [nome, String(inicial?.[nome] ?? '')])),
    categoriaAtrativoId: String(inicial?.categoria?.id ?? ''),
    gratuito: inicial?.gratuito ?? true,
    acessivel: inicial?.acessivel ?? false,
    situacao: inicial?.situacao ?? '',
  }))
  const [erros, setErros] = useState({})
  const [erroGeral, setErroGeral] = useState('')
  const [salvando, setSalvando] = useState(false)
  const bloqueado = useRef(false)
  const formulario = useRef(null)
  const focoPendente = useRef(false)

  useEffect(() => {
    // Aguarda os campos serem reabilitados e as mensagens entrarem no DOM.
    if (!salvando && focoPendente.current) {
      focoPendente.current = false
      const primeiro = [...formulario.current.elements].find((elemento) => erros[elemento.name])
      primeiro?.focus()
    }
  }, [erros, salvando])

  function alterar(nome, valor) {
    setValores((atual) => ({ ...atual, [nome]: valor, ...(nome === 'gratuito' && valor ? { valorEntrada: '' } : {}) }))
    setErros((atual) => ({ ...atual, [nome]: undefined, ...(nome === 'gratuito' ? { valorEntrada: undefined } : {}) }))
    setErroGeral('')
  }

  function mostrarErros(novos) {
    focoPendente.current = true
    setErros(novos)
  }

  async function enviar(evento) {
    evento.preventDefault()
    if (bloqueado.current || categorias.length === 0) return
    const novos = {}
    for (const nome of ['nome', 'resumo', 'descricao']) {
      if (!valores[nome].trim()) novos[nome] = 'Preencha este campo obrigatório.'
    }
    for (const [nome, limite] of Object.entries(limites)) {
      if (valores[nome].length > limite) novos[nome] = `Use no máximo ${limite} caracteres.`
    }
    if (!categorias.some((categoria) => String(categoria.id) === valores.categoriaAtrativoId)) novos.categoriaAtrativoId = 'Selecione uma categoria ativa.'
    if (!Object.hasOwn(situacoes, valores.situacao)) novos.situacao = 'Selecione a situação.'
    for (const [nome, limite] of [['latitude', 90], ['longitude', 180]]) {
      const valor = valores[nome].trim()
      if (valor && (!/^[+-]?(\d+(\.\d*)?|\.\d+)$/.test(valor) || !Number.isFinite(Number(valor)) || Math.abs(Number(valor)) > limite)) novos[nome] = `Informe um número entre -${limite} e ${limite}.`
    }
    if (Boolean(valores.latitude.trim()) !== Boolean(valores.longitude.trim())) {
      novos[valores.latitude.trim() ? 'longitude' : 'latitude'] = 'Informe latitude e longitude juntas.'
    }
    if (valores.site.trim()) {
      try {
        const url = new URL(valores.site.trim())
        if (!['https:', 'http:'].includes(url.protocol) || !url.hostname) throw new Error()
      } catch { novos.site = 'Informe uma URL válida, começando com https:// ou http://.' }
    }
    const preco = valores.valorEntrada.trim().replace(',', '.')
    if (!valores.gratuito && preco && (!/^\d+(\.\d{1,2})?$/.test(preco) || Number(preco) > 99999999.99)) novos.valorEntrada = 'Informe um valor não negativo, com até oito dígitos inteiros e duas casas decimais.'
    if (Object.keys(novos).length) { mostrarErros(novos); return }

    const opcional = (nome) => valores[nome].trim() || null
    const numero = (nome) => opcional(nome) === null ? null : Number(valores[nome])
    const payload = {
      nome: valores.nome.trim(), resumo: valores.resumo.trim(), descricao: valores.descricao.trim(),
      endereco: opcional('endereco'), latitude: numero('latitude'), longitude: numero('longitude'),
      telefone: opcional('telefone'), site: opcional('site'), gratuito: valores.gratuito,
      valorEntrada: valores.gratuito || !preco ? null : Number(preco), acessivel: valores.acessivel,
      situacao: valores.situacao, categoriaAtrativoId: Number(valores.categoriaAtrativoId),
    }
    bloqueado.current = true
    setSalvando(true)
    setErros({})
    setErroGeral('')
    try { await onSalvar(payload) } catch (falha) {
      const status = falha.response?.status
      const campos = falha.response?.data?.campos
      if (status === 400 && campos && typeof campos === 'object') {
        mostrarErros(Object.fromEntries(Object.entries(campos).filter(([nome, mensagem]) => Object.hasOwn(valores, nome) && typeof mensagem === 'string')))
      }
      setErroGeral(status === 403 ? 'Operação não autorizada. Sua conta precisa de permissão administrativa.'
        : status === 404 ? (edicao ? 'Atrativo ou categoria não encontrado ou desativado. Volte à listagem e tente novamente.' : 'Categoria não encontrada ou desativada. Recarregue as categorias.')
          : status === 401 ? 'Sua sessão expirou. Entre novamente para continuar.'
            : 'Não foi possível salvar o atrativo. Confira os campos e tente novamente.')
    } finally { bloqueado.current = false; setSalvando(false) }
  }

  function campo(nome, label, { tipo = 'text', obrigatorio = false, opcoes, texto = false, dica, ...props } = {}) {
    const comuns = {
      id: nome, name: nome, value: typeof valores[nome] === 'boolean' ? String(valores[nome]) : valores[nome],
      onChange: (evento) => alterar(nome, typeof valores[nome] === 'boolean' ? evento.target.value === 'true' : evento.target.value),
      required: obrigatorio, 'aria-invalid': Boolean(erros[nome]),
      'aria-describedby': [dica && `${nome}-dica`, erros[nome] && `${nome}-erro`].filter(Boolean).join(' ') || undefined,
      className: estiloCampo, ...props,
    }
    return <div className="min-w-0">
      <label htmlFor={nome} className="mb-2 block font-semibold text-floresta">{label}{obrigatorio ? ' *' : ' (opcional)'}</label>
      {opcoes ? <select {...comuns}>{opcoes.map(([valor, rotulo]) => <option key={valor} value={valor}>{rotulo}</option>)}</select>
        : texto ? <textarea {...comuns} rows={nome === 'descricao' ? 6 : 3} /> : <input {...comuns} type={tipo} />}
      {dica && <p id={`${nome}-dica`} className="mt-1 text-sm text-slate-600">{dica}</p>}
      {erros[nome] && <p id={`${nome}-erro`} role="alert" className="mt-2 text-sm font-medium text-red-800">{erros[nome]}</p>}
    </div>
  }

  return <form ref={formulario} onSubmit={enviar} noValidate className="space-y-6" aria-busy={salvando}>
    <p className="text-sm text-slate-600">Campos com * são obrigatórios.</p>
    {categorias.length === 0 && <p role="alert">Nenhuma categoria ativa disponível. O cadastro depende de uma categoria ativa.</p>}
    <fieldset disabled={salvando} className="min-w-0 space-y-5 rounded-2xl border border-floresta/10 bg-white p-5 sm:p-7">
      <legend className="px-2 text-lg font-bold text-floresta">Informações principais</legend>
      {campo('nome', 'Nome', { obrigatorio: true, maxLength: 150 })}
      {campo('resumo', 'Resumo', { obrigatorio: true, texto: true, maxLength: 300, dica: `${valores.resumo.length}/300 caracteres` })}
      {campo('descricao', 'Descrição', { obrigatorio: true, texto: true })}
      {campo('categoriaAtrativoId', 'Categoria', { obrigatorio: true, opcoes: [['', 'Selecione uma categoria'], ...categorias.map((categoria) => [String(categoria.id), categoria.nome])],
        dica: inicial?.categoria && !categorias.some((categoria) => categoria.id === inicial.categoria.id) ? 'A categoria original está indisponível. Selecione outra categoria ativa.' : undefined })}
    </fieldset>
    <fieldset disabled={salvando} className="min-w-0 space-y-5 rounded-2xl border border-floresta/10 bg-white p-5 sm:p-7">
      <legend className="px-2 text-lg font-bold text-floresta">Localização e contato</legend>
      {campo('endereco', 'Endereço', { maxLength: 255 })}
      <div className="grid gap-5 sm:grid-cols-2">
        {campo('latitude', 'Latitude', { inputMode: 'decimal', dica: 'Entre -90 e 90. Use ponto decimal.' })}
        {campo('longitude', 'Longitude', { inputMode: 'decimal', dica: 'Entre -180 e 180. Use ponto decimal.' })}
        {campo('telefone', 'Telefone', { tipo: 'tel', maxLength: 30 })}
        {campo('site', 'Site', { tipo: 'url', maxLength: 255, placeholder: 'https://exemplo.com' })}
      </div>
    </fieldset>
    <fieldset disabled={salvando} className="min-w-0 space-y-5 rounded-2xl border border-floresta/10 bg-white p-5 sm:p-7">
      <legend className="px-2 text-lg font-bold text-floresta">Visitação e acessibilidade</legend>
      <div className="grid gap-5 sm:grid-cols-2">
        {campo('gratuito', 'Gratuito', { obrigatorio: true, opcoes: [['true', 'Sim'], ['false', 'Não']] })}
        {campo('valorEntrada', 'Valor da entrada (R$)', { inputMode: 'decimal', disabled: valores.gratuito || salvando, dica: valores.gratuito ? 'Sem cobrança de entrada.' : 'Pode ficar vazio se o valor ainda não foi informado.' })}
      </div>
      {campo('acessivel', 'Acessível para pessoas com mobilidade reduzida', { obrigatorio: true, opcoes: [['true', 'Sim'], ['false', 'Não']] })}
      {campo('situacao', 'Situação', { obrigatorio: true, opcoes: [['', 'Selecione a situação'], ...Object.entries(situacoes)] })}
    </fieldset>
    {erroGeral && <p role="alert" className="rounded-xl border border-red-200 bg-white p-4 text-red-800">{erroGeral}</p>}
    <div className="flex flex-col gap-3 sm:flex-row sm:justify-end">
      {salvando ? <button type="button" disabled className="min-h-12 rounded-full border border-floresta px-6 text-slate-600">Cancelar</button>
        : <Link to="/admin/atrativos" className={`inline-flex min-h-12 items-center justify-center rounded-full border border-floresta px-6 font-semibold text-floresta ${foco}`}>Cancelar</Link>}
      <button type="submit" disabled={salvando || categorias.length === 0} className={`min-h-12 rounded-full bg-floresta px-8 font-semibold text-white hover:bg-folha disabled:bg-slate-600 ${foco}`}>{salvando ? 'Salvando…' : 'Salvar'}</button>
    </div>
    <p role="status" className="sr-only">{salvando ? 'Salvando atrativo. Aguarde.' : ''}</p>
  </form>
}
