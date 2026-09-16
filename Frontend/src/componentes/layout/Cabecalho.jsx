import { Link } from 'react-router-dom'
import logoGuapiVerde from '../../assets/Logo_GUAPIVERDE.png'

function Cabecalho() {
  return (
    <header className="sticky top-0 z-30 bg-floresta text-white shadow-md">
      <div className="mx-auto flex h-20 max-w-6xl items-center px-4 sm:h-24 sm:px-6">
        <Link
          to="/"
          className="flex h-full items-center rounded-lg"
          aria-label="Ir para a página inicial do Guapi Verde"
        >
          <img
            src={logoGuapiVerde}
            alt="Guapi Verde"
            className="h-16 w-auto max-w-[12rem] object-contain sm:h-20 sm:max-w-[15rem]"
          />
        </Link>
      </div>
    </header>
  )
}

export default Cabecalho
