import { ArrowLeft } from 'lucide-react'
import { Link } from 'react-router-dom'

function DetalheAtrativo() {
  return (
    <section className="px-4 py-12 text-center sm:px-6">
      <h1 className="text-3xl font-bold text-floresta">Detalhes em construção</h1>
      <p className="mx-auto mt-3 max-w-md text-slate-600">
        As informações completas dos atrativos serão conectadas à API em uma próxima etapa.
      </p>
      <Link to="/explorar" className="mt-6 inline-flex items-center gap-2 font-bold text-folha hover:underline">
        <ArrowLeft size={18} aria-hidden="true" /> Voltar para explorar
      </Link>
    </section>
  )
}

export default DetalheAtrativo
