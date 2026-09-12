import { Leaf, LogOut, UserRound } from 'lucide-react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import Favoritos from './componentes/Favoritos'
import Preferencias from './componentes/Preferencias'

function Perfil() {
  const { usuario, autenticado, carregandoInicial, logout } = useAuth()
  const navegar = useNavigate()

  function sair() {
    logout()
    navegar('/', { replace: true })
  }

  if (carregandoInicial) {
    return <p role="status" className="mx-4 mt-4 rounded-2xl border border-floresta/10 bg-white p-5 text-slate-600 shadow-sm sm:mx-6">Carregando seu perfil...</p>
  }

  if (!autenticado) {
    return (
      <div className="-mb-28 flex min-h-[calc(100dvh-4rem)] items-center justify-center px-4 pt-6 pb-[calc(5.5rem+1px+max(0.75rem,env(safe-area-inset-bottom)))] sm:px-6">
        <section className="w-full max-w-lg rounded-2xl border border-floresta/10 bg-white p-6 text-center shadow-sm sm:p-8">
          <Leaf aria-hidden="true" className="mx-auto mb-3 size-10 text-folha" />
          <h1 className="text-2xl font-bold text-floresta sm:text-3xl">Seu cantinho no Guapi Verde</h1>
          <p className="mt-3 text-sm leading-relaxed text-slate-600 sm:text-base">Entre para acessar seus atrativos favoritos e escolher as categorias que você mais gosta de explorar.</p>
          <div className="mt-5 flex flex-col justify-center gap-3 sm:flex-row">
            <Link to="/login" className="flex min-h-12 items-center justify-center rounded-full border border-floresta bg-floresta px-6 py-2.5 font-bold text-white hover:bg-folha sm:flex-1">Entrar</Link>
            <Link to="/cadastro" className="flex min-h-12 items-center justify-center rounded-full border border-floresta px-6 py-2.5 font-bold text-floresta hover:bg-creme sm:flex-1">Criar conta</Link>
          </div>
        </section>
      </div>
    )
  }

  return (
    <div className="mx-auto -mb-28 max-w-5xl space-y-4 px-4 pt-4 pb-[calc(6rem+env(safe-area-inset-bottom))] sm:space-y-5 sm:px-6 sm:pt-6">
      <section className="flex flex-col gap-3 rounded-2xl border border-floresta/10 bg-white p-4 shadow-sm sm:flex-row sm:items-center sm:justify-between sm:p-5">
        <div className="flex min-w-0 items-center gap-3">
          <UserRound aria-hidden="true" className="size-10 shrink-0 rounded-xl bg-creme p-2 text-folha" />
          <div className="min-w-0">
            <h1 className="text-xl font-bold text-floresta">Meu perfil</h1>
            <p className="mt-1 [overflow-wrap:anywhere] font-semibold">{usuario.nome}</p>
            <p className="text-sm [overflow-wrap:anywhere] text-slate-600">{usuario.email}</p>
            <p className="mt-1.5 inline-block rounded-full bg-floresta/10 px-2.5 py-0.5 text-xs font-semibold text-floresta">{usuario.perfil === 'ADMIN' ? 'Administrador' : 'Visitante'}</p>
          </div>
        </div>
        <button type="button" onClick={sair} className="flex min-h-11 shrink-0 items-center justify-center gap-2 rounded-full border border-floresta px-5 py-2 text-sm font-bold text-floresta hover:bg-creme">
          <LogOut aria-hidden="true" className="size-4" /> Sair
        </button>
      </section>
      <Favoritos key={`favoritos-${usuario.usuarioId}`} />
      <Preferencias key={`preferencias-${usuario.usuarioId}`} />
    </div>
  )
}

export default Perfil
