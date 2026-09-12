import { useEffect, useRef, useState } from 'react'
import { Heart, Leaf, LoaderCircle, Trash2 } from 'lucide-react'
import { Link } from 'react-router-dom'
import { listarFavoritos, removerFavorito } from '../../../servicos/favoritoService'

function Favoritos() {
  const [favoritos, setFavoritos] = useState([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState(false)
  const [tentativa, setTentativa] = useState(0)
  const [removendo, setRemovendo] = useState({})
  const [errosRemocao, setErrosRemocao] = useState({})
  const operacoes = useRef(new Set())

  useEffect(() => {
    const controller = new AbortController()
    // Evita iniciar duas consultas no ciclo de montagem do StrictMode.
    const inicio = setTimeout(async () => {
      try {
        const dados = await listarFavoritos(controller.signal)
        if (!controller.signal.aborted) setFavoritos(dados)
      } catch {
        if (!controller.signal.aborted) setErro(true)
      } finally {
        if (!controller.signal.aborted) setCarregando(false)
      }
    }, 0)
    return () => {
      clearTimeout(inicio)
      controller.abort()
    }
  }, [tentativa])

  function tentarNovamente() {
    setErro(false)
    setCarregando(true)
    setTentativa((atual) => atual + 1)
  }

  async function remover(atrativoId) {
    if (operacoes.current.has(atrativoId)) return
    operacoes.current.add(atrativoId)
    setRemovendo((atual) => ({ ...atual, [atrativoId]: true }))
    setErrosRemocao((atual) => ({ ...atual, [atrativoId]: false }))
    try {
      await removerFavorito(atrativoId)
      setFavoritos((atuais) => atuais.filter((favorito) => favorito.atrativoId !== atrativoId))
    } catch {
      setErrosRemocao((atual) => ({ ...atual, [atrativoId]: true }))
    } finally {
      operacoes.current.delete(atrativoId)
      setRemovendo((atual) => ({ ...atual, [atrativoId]: false }))
    }
  }

  return (
    <section aria-labelledby="titulo-favoritos" className="rounded-2xl border border-floresta/10 bg-white p-4 shadow-sm sm:p-5">
      <h2 id="titulo-favoritos" className="mb-3 flex items-center gap-2 text-xl font-bold text-floresta"><Heart aria-hidden="true" className="size-5 shrink-0" /> Meus favoritos</h2>
      {carregando ? <p role="status" className="text-sm leading-relaxed text-slate-600">Carregando favoritos...</p>
        : erro ? <div role="alert" className="text-sm leading-relaxed text-slate-600">
          <p>Não foi possível carregar seus favoritos.</p>
          <button type="button" onClick={tentarNovamente} className="mt-3 rounded-full bg-floresta px-5 py-3 font-bold text-white hover:bg-folha">Tentar novamente</button>
        </div>
          : favoritos.length === 0 ? <div className="text-sm leading-relaxed text-slate-600">
            <p>Você ainda não tem favoritos. Explore os atrativos e descubra seus próximos passeios.</p>
            <Link to="/explorar" className="mt-2 inline-flex min-h-11 items-center rounded font-semibold text-floresta underline underline-offset-4">Explorar atrativos</Link>
          </div>
            : <ul className="grid gap-3 md:grid-cols-2">
              {favoritos.map((favorito) => (
                <li key={favorito.atrativoId} className="flex min-w-0 flex-col rounded-xl border border-floresta/10 bg-white p-4 text-sm leading-relaxed text-slate-600">
                  <Leaf aria-hidden="true" className="mb-3 size-10 rounded-xl bg-creme p-2 text-folha" />
                  {favorito.categoriaAtrativoNome && <p className="[overflow-wrap:anywhere] text-sm font-semibold text-folha">{favorito.categoriaAtrativoNome}</p>}
                  <h3 className="mt-1 [overflow-wrap:anywhere] text-lg font-bold text-floresta"><Link to={`/atrativos/${favorito.atrativoId}`} className="rounded hover:underline">{favorito.atrativoNome}</Link></h3>
                  {favorito.atrativoResumo && <p className="mt-2 [overflow-wrap:anywhere] text-slate-600">{favorito.atrativoResumo}</p>}
                  <div className="mt-auto pt-4">
                    <button type="button" disabled={removendo[favorito.atrativoId]} onClick={() => remover(favorito.atrativoId)} aria-label={`Remover ${favorito.atrativoNome} dos favoritos`} className="flex items-center gap-2 rounded-full border border-slate-300 px-4 py-3 text-sm font-semibold hover:bg-creme disabled:cursor-wait disabled:opacity-60">
                      {removendo[favorito.atrativoId] ? <LoaderCircle aria-hidden="true" className="size-4 animate-spin" /> : <Trash2 aria-hidden="true" className="size-4" />}
                      {removendo[favorito.atrativoId] ? 'Removendo...' : 'Remover dos favoritos'}
                    </button>
                    {errosRemocao[favorito.atrativoId] && <p role="alert" className="mt-2 text-sm text-red-700">Não foi possível remover. Tente novamente.</p>}
                  </div>
                </li>
              ))}
            </ul>}
    </section>
  )
}

export default Favoritos
