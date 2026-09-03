import { ArrowUpRight, Leaf } from 'lucide-react'
import { Link } from 'react-router-dom'

function CartaoAtrativo({ atrativo }) {
  const imagem = atrativo.imagemSelecionada

  return (
    <article className="group overflow-hidden rounded-3xl bg-white shadow-[0_8px_24px_rgba(23,77,54,0.09)]">
      <div className="relative grid aspect-[4/3] place-items-center overflow-hidden bg-folha/10 text-folha">
        <Leaf size={48} aria-hidden="true" />
        {imagem && (
          <img
            src={imagem.url}
            alt={imagem.textoAlternativo || `Imagem de ${atrativo.nome}`}
            className="absolute inset-0 size-full object-cover transition duration-500 group-hover:scale-105"
            onError={(evento) => {
              evento.currentTarget.hidden = true
            }}
          />
        )}
        <span className="absolute left-3 top-3 rounded-full bg-white/90 px-3 py-1 text-xs font-bold text-floresta shadow-sm">
          {atrativo.categoria.nome}
        </span>
      </div>
      <div className="p-4">
        <div className="flex items-start justify-between gap-3">
          <div className="min-w-0">
            <h3 className="font-bold text-slate-900">{atrativo.nome}</h3>
            {atrativo.resumo && <p className="mt-1 line-clamp-2 text-sm text-slate-500">{atrativo.resumo}</p>}
            <div className="mt-3 flex flex-wrap gap-2 text-xs font-semibold text-folha">
              {atrativo.gratuito && <span>Entrada gratuita</span>}
              {atrativo.acessivel && <span>Acessível</span>}
            </div>
          </div>
          <Link
            to={`/atrativos/${atrativo.id}`}
            className="grid size-10 shrink-0 place-items-center rounded-full bg-floresta text-white transition hover:bg-folha"
            aria-label={`Ver detalhes de ${atrativo.nome}`}
          >
            <ArrowUpRight size={19} aria-hidden="true" />
          </Link>
        </div>
      </div>
    </article>
  )
}

export default CartaoAtrativo
