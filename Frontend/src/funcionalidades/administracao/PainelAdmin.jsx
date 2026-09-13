import { CalendarDays, ListFilter, MapPin, ShieldCheck, Ticket } from 'lucide-react'
import { Link } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'

const acessos = [
  { caminho: '/explorar', titulo: 'Visualizar atrativos', descricao: 'Explore os atrativos disponíveis para os visitantes.', Icone: MapPin },
  { caminho: '/agenda', titulo: 'Visualizar agenda', descricao: 'Confira a programação pública do Guapi Verde.', Icone: CalendarDays },
  { caminho: '/beneficios', titulo: 'Visualizar benefícios', descricao: 'Veja a página de benefícios disponível ao público.', Icone: Ticket },
]

function PainelAdmin() {
  const { usuario } = useAuth()

  return (
    <div className="space-y-8 px-4 py-6 sm:px-6 sm:py-8">
      <section aria-labelledby="titulo-painel-admin" className="rounded-2xl border border-floresta/10 bg-white p-6 shadow-sm sm:p-8">
        <span className="inline-flex items-center gap-2 rounded-full bg-folha/10 px-3 py-1 text-sm font-semibold text-floresta">
          <ShieldCheck aria-hidden="true" className="size-4" /> Administrador
        </span>
        <h1 id="titulo-painel-admin" className="mt-4 text-2xl font-bold text-floresta sm:text-3xl">Painel administrativo</h1>
        <p className="mt-3 text-lg font-semibold [overflow-wrap:anywhere]">Olá, {usuario.nome}!</p>
        <p className="mt-2 max-w-2xl leading-relaxed text-slate-600">Consulte os atrativos ativos cadastrados e acesse as páginas públicas do Guapi Verde.</p>
      </section>

      <Link to="/admin/atrativos" className="block rounded-2xl border border-floresta/10 bg-white p-6 shadow-sm transition-colors hover:bg-folha/5 focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta">
        <ListFilter aria-hidden="true" className="mb-4 size-7 text-folha" />
        <h2 className="text-xl font-bold text-floresta">Gerenciar atrativos</h2>
        <p className="mt-2 text-sm leading-relaxed text-slate-600">Consulte e organize os atrativos cadastrados.</p>
      </Link>

        <Link to="/admin/categorias" className="mb-8 block rounded-2xl border border-floresta/10 bg-white p-6 shadow-sm transition-colors hover:bg-folha/5 focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta">
          <ListFilter aria-hidden="true" className="mb-4 size-7 text-folha" />
          <h2 className="text-xl font-bold text-floresta">Gerenciar categorias</h2>
          <p className="mt-2 text-sm leading-relaxed text-slate-600">Organize as categorias utilizadas pelos atrativos.</p>
        </Link>
      <section aria-labelledby="titulo-acessos-publicos">
        <h2 id="titulo-acessos-publicos" className="text-xl font-bold text-floresta">Acessos rápidos</h2>
        <p className="mt-2 text-sm leading-relaxed text-slate-600">Os links abrem o conteúdo público para visualização. A edição ainda não está disponível.</p>
        <div className="mt-4 grid gap-4 md:grid-cols-3">
          {acessos.map(({ caminho, titulo, descricao, Icone }) => (
            <Link key={caminho} to={caminho} className="rounded-2xl border border-floresta/10 bg-white p-5 shadow-sm transition-colors hover:bg-folha/5">
              <Icone aria-hidden="true" className="mb-4 size-7 text-folha" />
              <h3 className="font-bold text-floresta">{titulo}</h3>
              <p className="mt-2 text-sm leading-relaxed text-slate-600">{descricao}</p>
            </Link>
          ))}
        </div>
      </section>
    </div>
  )
}

export default PainelAdmin
