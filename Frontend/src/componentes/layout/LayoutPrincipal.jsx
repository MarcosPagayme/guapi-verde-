import { Outlet } from 'react-router-dom'
import Cabecalho from './Cabecalho'
import NavegacaoInferior from './NavegacaoInferior'

function LayoutPrincipal() {
  return (
    <div className="min-h-screen bg-creme">
      <Cabecalho />
      <main className="mx-auto min-h-[calc(100vh-4rem)] max-w-6xl pb-28">
        <Outlet />
      </main>
      <NavegacaoInferior />
    </div>
  )
}

export default LayoutPrincipal
