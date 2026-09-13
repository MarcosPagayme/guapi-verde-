import { useRef, useState } from 'react'
import { LoaderCircle } from 'lucide-react'
import DialogoHorario from './DialogoHorario'
import { nomeDia } from './diasSemana'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'

export default function ExcluirHorario({ horario, origem, focoAlternativo, onCancelar, onExcluir, onSincronizar }) {
  const [excluindo, setExcluindo] = useState(false)
  const [erro, setErro] = useState('')
  const [indisponivel, setIndisponivel] = useState(false)
  const bloqueado = useRef(false)
  function fechar() { if (!bloqueado.current) onCancelar() }
  async function excluir() {
    if (bloqueado.current || indisponivel) return
    bloqueado.current = true
    setExcluindo(true)
    setErro('')
    try { await onExcluir(horario.id) } catch (falha) {
      const status = falha.response?.status
      setErro(status === 403 ? 'Sua conta não possui permissão para excluir horários.'
        : status === 401 ? 'Sua sessão expirou. Entre novamente para continuar.'
          : status === 404 ? 'Este horário não está mais disponível. A página será sincronizada. Feche esta confirmação para continuar.'
            : 'Não foi possível excluir o horário. Tente novamente.')
      if (status === 404) { setIndisponivel(true); onSincronizar() }
    } finally { bloqueado.current = false; setExcluindo(false) }
  }
  return <DialogoHorario titulo="Excluir horário?" origem={origem} focoAlternativo={focoAlternativo} onCancelar={fechar}>
    <p className="mt-4 font-semibold [overflow-wrap:anywhere]">{nomeDia(horario.diaSemana)}</p>
    <p className="mt-3">A exclusão é permanente e não pode ser desfeita.</p>
    {erro && <p role="alert" className="mt-4 rounded-xl bg-red-50 p-3 text-red-800">{erro}</p>}
    <div className="mt-6 flex flex-col gap-3 sm:flex-row sm:justify-end" aria-busy={excluindo}>
      <button data-foco-inicial type="button" disabled={excluindo} onClick={fechar} className={`min-h-12 rounded-full border border-floresta px-5 py-3 font-semibold text-floresta ${foco}`}>Cancelar</button>
      <button type="button" disabled={excluindo || indisponivel} onClick={excluir} className={`inline-flex min-h-12 items-center justify-center gap-2 rounded-full bg-red-800 px-5 py-3 font-semibold text-white disabled:bg-slate-600 ${foco}`}>
        {excluindo && <LoaderCircle aria-hidden="true" className="size-5 animate-spin motion-reduce:animate-none" />}{excluindo ? 'Excluindo…' : 'Excluir horário'}
      </button>
    </div>
    <p role="status" className="sr-only">{excluindo ? 'Excluindo horário. Aguarde.' : ''}</p>
  </DialogoHorario>
}
