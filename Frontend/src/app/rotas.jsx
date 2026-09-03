import { createBrowserRouter } from 'react-router-dom'
import LayoutPrincipal from '../componentes/layout/LayoutPrincipal'
import Agenda from '../funcionalidades/agenda/Agenda'
import PainelAdmin from '../funcionalidades/administracao/PainelAdmin'
import Cadastro from '../funcionalidades/autenticacao/Cadastro'
import Login from '../funcionalidades/autenticacao/Login'
import DetalheAtrativo from '../funcionalidades/atrativos/DetalheAtrativo'
import Explorar from '../funcionalidades/atrativos/Explorar'
import Beneficios from '../funcionalidades/beneficios/Beneficios'
import Inicio from '../funcionalidades/inicio/Inicio'
import Perfil from '../funcionalidades/perfil/Perfil'

export const rotas = createBrowserRouter([
  {
    element: <LayoutPrincipal />,
    children: [
      { path: '/', element: <Inicio /> },
      { path: '/explorar', element: <Explorar /> },
      { path: '/atrativos/:id', element: <DetalheAtrativo /> },
      { path: '/agenda', element: <Agenda /> },
      { path: '/beneficios', element: <Beneficios /> },
      { path: '/perfil', element: <Perfil /> },
      { path: '/login', element: <Login /> },
      { path: '/cadastro', element: <Cadastro /> },
      { path: '/admin', element: <PainelAdmin /> },
    ],
  },
])
