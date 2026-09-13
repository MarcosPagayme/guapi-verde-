import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { atualizarAtrativo, cadastrarAtrativo, obterAtrativoPorId } from '../../../servicos/atrativoService'
import { listarCategoriasAtrativos } from '../../../servicos/categoriaAtrativoService'
import FormularioAtrativo from './componentes/FormularioAtrativo'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'

export default function SalvarAtrativo() {
  const { id } = useParams()
  return <PaginaAtrativo key={id ?? 'novo'} id={id} />
}

function PaginaAtrativo({ id }) {
  const navigate = useNavigate()
  const [dados, setDados] = useState(null)
  const [erro, setErro] = useState('')
  const [tentativa, setTentativa] = useState(0)

  useEffect(() => {
    let ativo = true
    async function carregar() {
      try {
        const [categorias, atrativo] = await Promise.all([
          listarCategoriasAtrativos().catch(() => { throw new Error('Não foi possível carregar as categorias. Tente novamente.') }),
          id ? obterAtrativoPorId(id).catch((falha) => {
            throw new Error(falha.response?.status === 404 ? 'Atrativo não encontrado ou desativado.' : 'Não foi possível carregar o atrativo. Tente novamente.')
          }) : null,
        ])
        if (ativo) setDados({ categorias: categorias.filter((categoria) => categoria.ativo), atrativo })
      } catch (falha) {
        if (ativo) setErro(falha.message)
      }
    }
    // O primeiro efeito do StrictMode é limpo antes de iniciar as consultas.
    const inicio = setTimeout(carregar, 0)
    return () => {
      ativo = false
      clearTimeout(inicio)
    }
  }, [id, tentativa])

  async function salvar(payload) {
    if (id) await atualizarAtrativo(id, payload)
    else await cadastrarAtrativo(payload)
    navigate('/admin/atrativos', { replace: true, state: { sucesso: id ? 'Atrativo atualizado com sucesso' : 'Atrativo cadastrado com sucesso' } })
  }

  return (
    <section className="mx-auto max-w-4xl space-y-6 px-4 pt-6 pb-28 sm:px-6 sm:pt-8" aria-labelledby="titulo-formulario">
      <Link to="/admin/atrativos" className={`inline-flex min-h-12 items-center rounded-lg font-semibold text-floresta ${foco}`}>← Voltar à listagem</Link>
      <h1 id="titulo-formulario" className="text-2xl font-bold text-floresta sm:text-3xl">{id ? 'Editar atrativo' : 'Novo atrativo'}</h1>
      {!dados && !erro && <p role="status">Carregando {id ? 'atrativo e categorias' : 'categorias'}…</p>}
      {erro && <div className="rounded-2xl bg-white p-6">
        <p role="alert">{erro}</p>
        <button type="button" onClick={() => { setErro(''); setTentativa((valor) => valor + 1) }} className={`mt-4 min-h-12 rounded-full bg-floresta px-6 font-semibold text-white ${foco}`}>Tentar novamente</button>
      </div>}
      {dados && <FormularioAtrativo inicial={dados.atrativo} categorias={dados.categorias} onSalvar={salvar} edicao={Boolean(id)} />}
    </section>
  )
}
