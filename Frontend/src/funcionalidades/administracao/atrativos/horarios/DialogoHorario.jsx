import { useEffect, useRef } from 'react'

export default function DialogoHorario({ titulo, origem, focoAlternativo, onCancelar, children }) {
  const dialogo = useRef(null)
  useEffect(() => {
    const elemento = dialogo.current
    const alternativa = focoAlternativo.current
    elemento.showModal()
    elemento.querySelector('[data-foco-inicial]')?.focus()
    return () => {
      elemento.close()
      if (origem?.isConnected) origem.focus()
      else alternativa?.focus()
    }
  }, [origem, focoAlternativo])

  return <dialog ref={dialogo} aria-labelledby="titulo-dialogo-horario" onCancel={(evento) => { evento.preventDefault(); onCancelar() }} className="m-auto max-h-[calc(100dvh-2rem)] w-[calc(100%-2rem)] max-w-xl overflow-y-auto rounded-2xl border border-floresta/10 bg-white p-5 text-slate-700 shadow-xl backdrop:bg-black/50 sm:p-7">
    <h2 id="titulo-dialogo-horario" className="mb-5 text-2xl font-bold text-floresta">{titulo}</h2>
    {children}
  </dialog>
}
