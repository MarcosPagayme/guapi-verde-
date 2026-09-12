import { LoaderCircle, ShieldAlert } from 'lucide-react'
import { Link, Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'

function RotaAdmin() {
  const { usuario, autenticado, carregandoInicial } = useAuth()

  if (carregandoInicial) {
    return (
      <p role="status" className="mx-4 mt-6 flex items-center gap-3 rounded-2xl border border-floresta/10 bg-white p-5 text-floresta shadow-sm sm:mx-6">
        <LoaderCircle aria-hidden="true" className="size-5 shrink-0 animate-spin motion-reduce:animate-none" />
        Validando sua sessão…
      </p>
    )
  }

  if (!autenticado) {
    return <Navigate to="/login" replace state={{ areaRestrita: true }} />
  }

  if (usuario?.perfil !== 'ADMIN') {
    return (
      <section aria-labelledby="titulo-acesso-negado" className="mx-auto max-w-xl px-4 py-8 sm:px-6 sm:py-12">
        <div className="rounded-2xl border border-floresta/10 bg-white p-6 shadow-sm sm:p-8">
          <ShieldAlert aria-hidden="true" className="mb-4 size-9 text-folha" />
          <h1 id="titulo-acesso-negado" className="text-2xl font-bold text-floresta sm:text-3xl">Acesso restrito</h1>
          <p className="mt-3 leading-relaxed text-slate-600">Sua conta não possui permissão administrativa. Você pode continuar explorando o Guapi Verde ou acessar seu perfil.</p>
          <div className="mt-6 flex flex-col gap-3 sm:flex-row">
            <Link to="/" className="flex min-h-12 items-center justify-center rounded-full bg-floresta px-6 py-3 font-semibold text-white hover:bg-folha">Voltar à Home</Link>
            <Link to="/perfil" className="flex min-h-12 items-center justify-center rounded-full border border-floresta px-6 py-3 font-semibold text-floresta hover:bg-creme">Acessar meu perfil</Link>
          </div>
        </div>
      </section>
    )
  }

  return <Outlet />
}

export default RotaAdmin
