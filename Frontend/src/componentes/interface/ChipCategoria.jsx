import { Link } from 'react-router-dom'

function ChipCategoria({ id, nome, Icone }) {
  return (
    <Link
      to={`/explorar?categoria=${encodeURIComponent(id)}`}
      className="flex min-w-24 flex-1 flex-col items-center gap-2 rounded-2xl border border-floresta/10 bg-white px-3 py-4 font-bold text-floresta shadow-sm transition hover:-translate-y-0.5 hover:shadow-md"
      aria-label={`Explorar ${nome}`}
    >
      <span className="grid size-11 place-items-center rounded-full bg-folha/10 text-folha">
        <Icone size={24} aria-hidden="true" />
      </span>
      <span className="text-sm">{nome}</span>
    </Link>
  )
}

export default ChipCategoria
