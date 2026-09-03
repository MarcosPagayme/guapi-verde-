import { CalendarDays, Compass, Gift, Home, UserRound } from 'lucide-react'
import { NavLink } from 'react-router-dom'

const itens = [
  { nome: 'Início', caminho: '/', Icone: Home },
  { nome: 'Explorar', caminho: '/explorar', Icone: Compass },
  { nome: 'Agenda', caminho: '/agenda', Icone: CalendarDays },
  { nome: 'Benefícios', caminho: '/beneficios', Icone: Gift },
  { nome: 'Perfil', caminho: '/perfil', Icone: UserRound },
]

function NavegacaoInferior() {
  return (
    <nav
      className="area-segura-inferior fixed inset-x-0 bottom-0 z-40 border-t border-black/10 bg-white/95 px-2 pt-2 shadow-[0_-8px_24px_rgba(23,77,54,0.08)] backdrop-blur"
      aria-label="Navegação principal"
    >
      <div className="mx-auto grid max-w-xl grid-cols-5">
        {itens.map(({ nome, caminho, Icone }) => (
          <NavLink
            key={caminho}
            to={caminho}
            end={caminho === '/'}
            className={({ isActive }) =>
              `flex min-h-14 flex-col items-center justify-center gap-1 rounded-xl px-1 text-[0.68rem] font-semibold transition sm:text-xs ${
                isActive ? 'bg-floresta/10 text-floresta' : 'text-slate-500 hover:text-floresta'
              }`
            }
            aria-label={`Abrir ${nome}`}
          >
            <Icone size={21} aria-hidden="true" />
            <span>{nome}</span>
          </NavLink>
        ))}
      </div>
    </nav>
  )
}

export default NavegacaoInferior
