import { useRef, useState } from 'react'
import { LoaderCircle } from 'lucide-react'
import DialogoCategoria from './DialogoCategoria'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'

export default function DesativarCategoria({ categoria, origem, focoAlternativo, permitido, onCancelar, onDesativar, onSincronizar }) {
  const [desativando, setDesativando] = useState(false)
  const [erro, setErro] = useState('')
  const bloqueado = useRef(false)
  function fechar() { if (!bloqueado.current) onCancelar() }
  async function desativar() {
    if (bloqueado.current || !permitido) return
    bloqueado.current = true
    setDesativando(true)
    setErro('')
    try { await onDesativar(categoria.id) } catch (falha) {
      const status = falha.response?.status
      setErro(falha.motivo === 'emUso' ? 'Esta categoria possui atrativos ativos vinculados e não pode ser desativada.'
        : falha.motivo === 'vinculos' ? 'Não foi possível verificar os vínculos. Feche esta confirmação e tente carregar os vínculos novamente.'
          : status === 403 ? 'Sua conta não possui permissão para desativar categorias.'
            : status === 401 ? 'Sua sessão expirou. Entre novamente para continuar.'
              : status === 404 ? 'Categoria não encontrada. A lista será atualizada; feche esta confirmação para conferir.'
                : 'Não foi possível desativar a categoria. Tente novamente.')
      if (status === 404) onSincronizar()
    } finally { bloqueado.current = false; setDesativando(false) }
  }
  return <DialogoCategoria titulo="Desativar categoria?" origem={origem} focoAlternativo={focoAlternativo} onCancelar={fechar}>
    <p className="font-semibold text-floresta [overflow-wrap:anywhere]">{categoria.nome}</p>
    <p className="mt-3">A categoria deixará de aparecer nas áreas públicas. Não existe reativação disponível nesta versão.</p>
    {erro && <p role="alert" className="mt-4 rounded-xl bg-red-50 p-3 text-red-800">{erro}</p>}
    {!permitido && !erro && <p role="status" className="mt-4">A desativação está bloqueada até confirmar que a categoria está ativa e sem vínculos.</p>}
    <div className="mt-6 flex flex-col gap-3 sm:flex-row sm:justify-end" aria-busy={desativando}>
      <button data-foco-inicial type="button" disabled={desativando} onClick={fechar} className={`min-h-12 rounded-full border border-floresta px-5 py-3 font-semibold text-floresta ${foco}`}>Cancelar</button>
      <button type="button" disabled={desativando || !permitido} onClick={desativar} className={`inline-flex min-h-12 items-center justify-center gap-2 rounded-full bg-red-800 px-5 py-3 font-semibold text-white disabled:bg-slate-600 ${foco}`}>
        {desativando && <LoaderCircle aria-hidden="true" className="size-5 animate-spin motion-reduce:animate-none" />}{desativando ? 'Desativando…' : 'Desativar categoria'}
      </button>
    </div>
    <p role="status" className="sr-only">{desativando ? 'Verificando vínculos e desativando categoria. Aguarde.' : ''}</p>
  </DialogoCategoria>
}
