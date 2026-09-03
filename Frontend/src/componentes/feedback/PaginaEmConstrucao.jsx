import { Construction } from 'lucide-react'
import { Link } from 'react-router-dom'

function PaginaEmConstrucao({ titulo, descricao }) {
  return (
    <section className="px-4 py-12 sm:px-6 sm:py-20">
      <div className="mx-auto max-w-xl rounded-3xl border border-floresta/10 bg-white p-8 text-center shadow-sm sm:p-12">
        <span className="mx-auto grid size-16 place-items-center rounded-full bg-dourado/15 text-floresta">
          <Construction size={30} aria-hidden="true" />
        </span>
        <p className="mt-5 text-sm font-bold uppercase tracking-[0.2em] text-folha">Em breve</p>
        <h1 className="mt-2 text-3xl font-bold text-floresta">{titulo}</h1>
        <p className="mx-auto mt-3 max-w-md leading-relaxed text-slate-600">{descricao}</p>
        <Link
          to="/"
          className="mt-7 inline-flex rounded-full bg-floresta px-6 py-3 font-bold text-white transition hover:bg-folha"
        >
          Voltar ao início
        </Link>
      </div>
    </section>
  )
}

export default PaginaEmConstrucao
