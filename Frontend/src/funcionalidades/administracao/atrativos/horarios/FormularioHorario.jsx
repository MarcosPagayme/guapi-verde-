import { useEffect, useRef, useState } from 'react'
import DialogoHorario from './DialogoHorario'
import { diasSemana, validarHorario } from './diasSemana'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'
const campo = `min-h-12 w-full min-w-0 rounded-xl border border-floresta/30 bg-white px-3 py-3 text-slate-800 disabled:bg-slate-100 disabled:text-slate-600 ${foco}`

export default function FormularioHorario({ horario, horarios, atrativoId, origem, focoAlternativo, onCancelar, onSalvar, onSincronizar, sincronizando }) {
  const [valores, setValores] = useState({
    diaSemana: horario?.diaSemana ?? '', fechado: horario?.fechado ?? false,
    horarioAbertura: horario?.fechado ? '' : horario?.horarioAbertura?.slice(0, 5) ?? '',
    horarioFechamento: horario?.fechado ? '' : horario?.horarioFechamento?.slice(0, 5) ?? '',
    observacao: horario?.observacao ?? '',
  })
  const [erros, setErros] = useState({})
  const [erroGeral, setErroGeral] = useState('')
  const [salvando, setSalvando] = useState(false)
  const bloqueado = useRef(false)
  const focoPendente = useRef(false)
  const formulario = useRef(null)

  useEffect(() => {
    if (!salvando && focoPendente.current) {
      focoPendente.current = false
      const primeiro = [...formulario.current.elements].find((item) => erros[item.name])
      primeiro?.focus()
    }
  }, [erros, salvando])

  const ocupado = (dia) => horarios.some((item) => item.id !== horario?.id && item.diaSemana === dia)
  function fechar() { if (!bloqueado.current) onCancelar() }
  function alterar(nome, valor) {
    setValores((atuais) => ({ ...atuais, [nome]: valor, ...(nome === 'fechado' && valor ? { horarioAbertura: '', horarioFechamento: '' } : {}) }))
    setErros((atuais) => ({ ...atuais, [nome]: undefined, ...(nome === 'fechado' ? { horarioAbertura: undefined, horarioFechamento: undefined } : {}) }))
    setErroGeral('')
  }
  async function enviar(evento) {
    evento.preventDefault()
    if (bloqueado.current || sincronizando) return
    const novos = validarHorario(valores, horarios, horario?.id)
    if (Object.keys(novos).length) { focoPendente.current = true; setErros(novos); return }
    bloqueado.current = true
    setSalvando(true)
    setErroGeral('')
    setErros({})
    try {
      await onSalvar({ atrativoId: Number(atrativoId), diaSemana: valores.diaSemana, fechado: valores.fechado,
        horarioAbertura: valores.fechado ? null : valores.horarioAbertura,
        horarioFechamento: valores.fechado ? null : valores.horarioFechamento,
        observacao: valores.observacao.trim() || null })
    } catch (falha) {
      const status = falha.response?.status
      const campos = falha.response?.data?.campos
      if (status === 400 && campos && typeof campos === 'object') {
        focoPendente.current = true
        setErros(Object.fromEntries(Object.entries(campos).filter(([nome, mensagem]) => Object.hasOwn(valores, nome) && typeof mensagem === 'string')))
      }
      setErroGeral(status === 403 ? 'Sua conta não possui permissão para gerenciar horários.'
        : status === 401 ? 'Sua sessão expirou. Entre novamente para continuar.'
          : status === 404 ? 'O horário ou o atrativo não está mais disponível. A página será sincronizada; feche o formulário para conferir.'
            : 'Não foi possível salvar o horário. Confira os campos e tente novamente.')
      if (status === 404) onSincronizar()
    } finally { bloqueado.current = false; setSalvando(false) }
  }
  function atributos(nome) {
    return { id: `horario-${nome}`, name: nome, value: valores[nome], onChange: (evento) => alterar(nome, evento.target.value),
      'aria-invalid': Boolean(erros[nome]), 'aria-describedby': `${nome}-ajuda${erros[nome] ? ` ${nome}-erro` : ''}`, className: campo }
  }
  return <DialogoHorario titulo={horario ? 'Editar horário' : 'Adicionar horário'} origem={origem} focoAlternativo={focoAlternativo} onCancelar={fechar}>
    <form ref={formulario} onSubmit={enviar} noValidate aria-busy={salvando} className="space-y-5">
      <fieldset disabled={salvando} className="min-w-0 space-y-5">
        <div>
          <label htmlFor="horario-diaSemana" className="mb-2 block font-semibold text-floresta">Dia da semana</label>
          <select {...atributos('diaSemana')} required data-foco-inicial><option value="">Selecione o dia</option>{diasSemana.map(([dia, nome]) => <option key={dia} value={dia} disabled={ocupado(dia)}>{nome}{ocupado(dia) ? ' (já cadastrado)' : ''}</option>)}</select>
          <p id="diaSemana-ajuda" className="mt-1 text-sm">Escolha um dia ainda disponível para este atrativo.</p>
          {erros.diaSemana && <p id="diaSemana-erro" role="alert" className="mt-2 text-red-800">{erros.diaSemana}</p>}
        </div>
        <div>
          <label htmlFor="horario-fechado" className="flex min-h-12 items-center gap-3 font-semibold text-floresta"><input id="horario-fechado" name="fechado" type="checkbox" checked={valores.fechado} onChange={(evento) => alterar('fechado', evento.target.checked)} aria-invalid={Boolean(erros.fechado)} aria-describedby={`fechado-ajuda${erros.fechado ? ' fechado-erro' : ''}`} className={`size-5 shrink-0 accent-floresta ${foco}`} />Fechado neste dia</label>
          <p id="fechado-ajuda" className="mt-1 text-sm">Ao marcar, os horários de abertura e fechamento serão apagados.</p>
          {erros.fechado && <p id="fechado-erro" role="alert" className="mt-2 text-red-800">{erros.fechado}</p>}
        </div>
        <div className="grid gap-5 sm:grid-cols-2">
          {[['horarioAbertura', 'Horário de abertura'], ['horarioFechamento', 'Horário de fechamento']].map(([nome, label]) => <div key={nome} className="min-w-0">
            <label htmlFor={`horario-${nome}`} className="mb-2 block font-semibold text-floresta">{label}</label>
            <input {...atributos(nome)} type="time" step={60} disabled={valores.fechado || salvando} required={!valores.fechado} />
            <p id={`${nome}-ajuda`} className="mt-1 text-sm">{valores.fechado ? 'Dia fechado: sem horário.' : nome === 'horarioAbertura' ? 'Obrigatório para dias abertos.' : 'Deve ser posterior à abertura, no mesmo dia.'}</p>
            {erros[nome] && <p id={`${nome}-erro`} role="alert" className="mt-2 text-red-800">{erros[nome]}</p>}
          </div>)}
        </div>
        <div>
          <label htmlFor="horario-observacao" className="mb-2 block font-semibold text-floresta">Observação (opcional)</label>
          <textarea {...atributos('observacao')} maxLength={255} rows={3} />
          <p id="observacao-ajuda" className="mt-1 text-sm">{valores.observacao.length}/255 caracteres</p>
          {erros.observacao && <p id="observacao-erro" role="alert" className="mt-2 text-red-800">{erros.observacao}</p>}
        </div>
      </fieldset>
      {erroGeral && <p role="alert" className="rounded-xl bg-red-50 p-3 text-red-800">{erroGeral}</p>}
      {sincronizando && <p role="status">Aguarde a atualização da página antes de salvar.</p>}
      <div className="flex flex-col gap-3 sm:flex-row sm:justify-end">
        <button type="button" disabled={salvando} onClick={fechar} className={`min-h-12 rounded-full border border-floresta px-5 py-3 font-semibold text-floresta ${foco}`}>Cancelar</button>
        <button type="submit" disabled={salvando || sincronizando} className={`min-h-12 rounded-full bg-floresta px-5 py-3 font-semibold text-white disabled:bg-slate-600 ${foco}`}>{salvando ? 'Salvando…' : horario ? 'Salvar alterações' : 'Adicionar horário'}</button>
      </div>
      <p role="status" className="sr-only">{salvando ? 'Salvando horário. Aguarde.' : ''}</p>
    </form>
  </DialogoHorario>
}
