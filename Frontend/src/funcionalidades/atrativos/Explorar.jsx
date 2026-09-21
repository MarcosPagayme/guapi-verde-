import { useEffect, useState } from 'react'
import CartaoAtrativo from '../../componentes/interface/CartaoAtrativo'
import { listarAtrativos } from '../../servicos/atrativoService'
import { listarCategoriasAtrativos } from '../../servicos/categoriaAtrativoService'
import { listarImagensDoAtrativo } from '../../servicos/imagemAtrativoService'

function Explorar() {
  const [atrativos, setAtrativos] = useState([])
  const [categorias, setCategorias] = useState([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState(null)
  const [busca, setBusca] = useState('')
  const [categoriaSelecionada, setCategoriaSelecionada] = useState('')

  async function carregarDados() {
    try {
      setCarregando(true)
      setErro(null)

      const [dadosAtrativos, dadosCategorias] = await Promise.all([
        listarAtrativos(),
        listarCategoriasAtrativos(),
      ])

      const atrativosComImagem = await Promise.all(
        dadosAtrativos.map(async (atrativo) => {
          const imagens = await listarImagensDoAtrativo(atrativo.id)

          const imagensOrdenadas = [...imagens].sort(
            (a, b) => (a.ordem ?? 0) - (b.ordem ?? 0),
          )

          const imagemSelecionada =
            imagensOrdenadas.find((imagem) => imagem.principal) ||
            imagensOrdenadas[0] ||
            null

          return {
            ...atrativo,
            imagemSelecionada,
          }
        }),
      )

      setAtrativos(atrativosComImagem)
      setCategorias(dadosCategorias)
    } catch (error) {
      console.error(error)
      setErro('Não foi possível carregar os atrativos.')
    } finally {
      setCarregando(false)
    }
  }

  useEffect(() => {
    carregarDados()
  }, [])

  const atrativosFiltrados = atrativos.filter((atrativo) => {
    const correspondeBusca = atrativo.nome
      .toLowerCase()
      .includes(busca.toLowerCase())

    const correspondeCategoria =
      !categoriaSelecionada ||
      String(atrativo.categoria.id) === categoriaSelecionada

    return correspondeBusca && correspondeCategoria
  })

  if (carregando) {
    return (
      <section className="px-4 py-8 sm:px-6 sm:py-12">
        <p className="text-slate-600">Carregando atrativos...</p>
      </section>
    )
  }

  if (erro) {
    return (
      <section className="px-4 py-8 sm:px-6 sm:py-12">
        <p className="text-slate-600">{erro}</p>

        <button
          type="button"
          onClick={carregarDados}
          className="mt-4 rounded-full bg-floresta px-5 py-2 font-semibold text-white"
        >
          Tentar novamente
        </button>
      </section>
    )
  }

  return (
    <section className="px-4 py-8 sm:px-6 sm:py-12">
      <p className="font-bold uppercase tracking-[0.18em] text-folha">
        Descubra Guapimirim
      </p>

      <h1 className="mt-2 text-3xl font-bold text-floresta sm:text-4xl">
        Explorar atrativos
      </h1>

      <div className="mt-6 flex flex-col gap-3 sm:flex-row">
        <input
          type="search"
          value={busca}
          onChange={(evento) => setBusca(evento.target.value)}
          placeholder="Buscar atrativo por nome..."
          className="w-full rounded-2xl border border-floresta/10 bg-white px-4 py-3 text-slate-700 outline-none focus:border-folha"
        />

        <select
          value={categoriaSelecionada}
          onChange={(evento) => setCategoriaSelecionada(evento.target.value)}
          className="rounded-2xl border border-floresta/10 bg-white px-4 py-3 text-slate-700 outline-none focus:border-folha sm:min-w-52"
        >
          <option value="">Todas as categorias</option>

          {categorias.map((categoria) => (
            <option key={categoria.id} value={categoria.id}>
              {categoria.nome}
            </option>
          ))}
        </select>
      </div>

      {atrativosFiltrados.length > 0 ? (
        <div className="mt-8 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {atrativosFiltrados.map((atrativo) => (
            <CartaoAtrativo key={atrativo.id} atrativo={atrativo} />
          ))}
        </div>
      ) : (
        <p className="mt-8 rounded-3xl border border-floresta/10 bg-white p-6 text-slate-600">
          Nenhum atrativo encontrado.
        </p>
      )}
    </section>
  )
}

export default Explorar