import { Leaf } from 'lucide-react'
import { Link } from 'react-router-dom'

function Cabecalho() {
  return (
    <header className="sticky top-0 z-30 bg-floresta text-white shadow-md">
      <div className="mx-auto flex max-w-6xl items-center gap-3 px-4 py-3 sm:px-6">
        <Link
          to="/"
          className="flex items-center gap-3 rounded-lg"
          aria-label="Ir para a página inicial do Guapi Verde"
        >
          <span className="grid size-10 place-items-center rounded-full bg-white/15">
            <Leaf aria-hidden="true" size={24} />
          </span>
          <span>
            <strong className="block text-lg leading-tight">Guapi Verde</strong>
            <span className="block text-xs text-white/75">Viva Guapimirim</span>
          </span>
        </Link>
      </div>
    </header>
  )
}

export default Cabecalho
