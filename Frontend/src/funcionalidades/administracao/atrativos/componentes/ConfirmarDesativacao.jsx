import { useEffect, useRef, useState } from 'react'
import { CircleOff, LoaderCircle } from 'lucide-react'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'

export default function ConfirmarDesativacao({ atrativo, onConfirmar, onCancelar, onIndisponivel, focoOrigem, focoAlternativo }) {
  const dialogo = useRef(null)
  const cancelar = useRef(null)
  const bloqueado = useRef(false)
  const [desativando, setDesativando] = useState(false)
  const [erro, setErro] = useState('')
  const [indisponivel, setIndisponivel] = useState(false)

  useEffect(() => {
    const elemento = dialogo.current
    const origem = focoOrigem.current
    const alternativa = focoAlternativo.current
    elemento.showModal()
    cancelar.current.focus()
    return () => {
      elemento.close()
      if (origem?.isConnected) origem.focus()
      else alternativa?.focus()
    }
  }, [focoOrigem, focoAlternativo])

  function fechar() {
    if (!bloqueado.current) onCancelar()
  }

  async function confirmar() {
    if (bloqueado.current || indisponivel) return
    bloqueado.current = true
    setDesativando(true)
    setErro('')
    try {
      await onConfirmar(atrativo.id)
    } catch (falha) {
      const status = falha.response?.status
      if (status === 404) {
        setIndisponivel(true)
        setErro('Este atrativo não está mais disponível. A listagem será atualizada. Feche esta confirmação para continuar.')
        onIndisponivel()
      } else {
        setErro(status === 403 ? 'Sua conta não possui permissão para desativar atrativos.'
          : status === 401 ? 'Sua sessão expirou. Entre novamente para continuar.'
            : 'Não foi possível desativar o atrativo. Tente novamente.')
      }
    } finally {
      bloqueado.current = false
      setDesativando(false)
    }
  }

  return (
    <dialog ref={dialogo} aria-labelledby="titulo-desativacao" aria-describedby="descricao-desativacao nome-desativacao" onCancel={(evento) => { evento.preventDefault(); fechar() }} className="m-auto max-h-[calc(100dvh-2rem)] w-[calc(100%-2rem)] max-w-lg overflow-y-auto rounded-2xl border border-floresta/10 bg-white p-5 text-slate-700 shadow-xl backdrop:bg-black/50 sm:p-7">
      <CircleOff aria-hidden="true" className="mb-3 size-8 text-red-800" />
      <h2 id="titulo-desativacao" className="text-2xl font-bold text-floresta">Desativar atrativo?</h2>
      <p id="nome-desativacao" className="mt-3 font-semibold text-floresta [overflow-wrap:anywhere]">{atrativo.nome}</p>
      <p id="descricao-desativacao" className="mt-3 leading-relaxed">O atrativo deixará de aparecer no aplicativo. Não existe reativação disponível nesta versão.</p>
      {erro && <p role="alert" className="mt-4 rounded-xl border border-red-200 bg-red-50 p-3 text-red-800">{erro}</p>}
      <div className="mt-6 flex flex-col gap-3 sm:flex-row sm:justify-end" aria-busy={desativando}>
        <button ref={cancelar} type="button" onClick={fechar} disabled={desativando} className={`min-h-12 rounded-full border border-floresta px-5 py-3 font-semibold text-floresta hover:bg-creme disabled:bg-slate-100 disabled:text-slate-600 ${foco}`}>Cancelar</button>
        <button type="button" onClick={confirmar} disabled={desativando || indisponivel} className={`inline-flex min-h-12 items-center justify-center gap-2 rounded-full bg-red-800 px-5 py-3 font-semibold text-white hover:bg-red-900 disabled:bg-slate-600 ${foco}`}>
          {desativando && <LoaderCircle aria-hidden="true" className="size-5 shrink-0 animate-spin motion-reduce:animate-none" />}
          {desativando ? 'Desativando…' : 'Desativar atrativo'}
        </button>
      </div>
      <p role="status" className="sr-only">{desativando ? 'Desativando atrativo. Aguarde.' : ''}</p>
    </dialog>
  )
}
