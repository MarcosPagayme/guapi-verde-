import { ArrowRight } from 'lucide-react'
import { Link } from 'react-router-dom'

function TituloSecao({ titulo, caminho }) {
  return (
    <div className="mb-4 flex items-center justify-between gap-4">
      <h2 className="text-xl font-bold tracking-tight text-floresta sm:text-2xl">{titulo}</h2>
      <Link
        to={caminho}
        className="flex shrink-0 items-center gap-1 rounded-md text-sm font-bold text-folha hover:underline"
      >
        Ver todos <ArrowRight size={16} aria-hidden="true" />
      </Link>
    </div>
  )
}

export default TituloSecao
