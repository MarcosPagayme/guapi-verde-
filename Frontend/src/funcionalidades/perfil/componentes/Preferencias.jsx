import { useEffect, useRef, useState } from 'react'
import { Check, LoaderCircle, Plus, SlidersHorizontal } from 'lucide-react'
import { listarCategoriasAtrativos } from '../../../servicos/categoriaAtrativoService'
import { adicionarPreferencia, listarPreferencias, removerPreferencia } from '../../../servicos/preferenciaService'

function Preferencias() {
  const [categorias, setCategorias] = useState([])
  const [preferencias, setPreferencias] = useState([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState(false)
  const [tentativa, setTentativa] = useState(0)
  const [atualizando, setAtualizando] = useState({})
  const [errosAtualizacao, setErrosAtualizacao] = useState({})
  const operacoes = useRef(new Set())

  useEffect(() => {
    const controller = new AbortController()
    // O agendamento também evita consultas duplicadas no StrictMode.
    const inicio = setTimeout(async () => {
      try {
        const [categoriasDisponiveis, preferenciasAtuais] = await Promise.all([
          listarCategoriasAtrativos(),
          listarPreferencias(controller.signal),
        ])
        if (controller.signal.aborted) return
        setCategorias(categoriasDisponiveis)
        setPreferencias(preferenciasAtuais)
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

  async function alternar(categoriaId, selecionada) {
    if (operacoes.current.has(categoriaId)) return
    operacoes.current.add(categoriaId)
    setAtualizando((atual) => ({ ...atual, [categoriaId]: true }))
    setErrosAtualizacao((atual) => ({ ...atual, [categoriaId]: false }))
    try {
      if (selecionada) {
        await removerPreferencia(categoriaId)
        setPreferencias((atuais) => atuais.filter((preferencia) => preferencia.categoriaAtrativoId !== categoriaId))
      } else {
        const adicionada = await adicionarPreferencia(categoriaId)
        setPreferencias((atuais) => [...atuais, adicionada])
      }
    } catch {
      setErrosAtualizacao((atual) => ({ ...atual, [categoriaId]: true }))
    } finally {
      operacoes.current.delete(categoriaId)
      setAtualizando((atual) => ({ ...atual, [categoriaId]: false }))
    }
  }

  const categoriasDisponiveis = categorias.filter((categoria) => categoria.ativo ||
    preferencias.some((preferencia) => preferencia.categoriaAtrativoId === categoria.id))

  return (
    <section aria-labelledby="titulo-preferencias" className="rounded-2xl border border-floresta/10 bg-white p-4 shadow-sm sm:p-5">
      <h2 id="titulo-preferencias" className="flex items-center gap-2 text-xl font-bold text-floresta"><SlidersHorizontal aria-hidden="true" className="size-5 shrink-0" /> Minhas preferências</h2>
      <p className="mt-2 text-sm leading-relaxed text-slate-600">Escolha as categorias que combinam com você. Toque em uma categoria selecionada para removê-la.</p>
      {carregando ? <p role="status" className="mt-3">Carregando preferências...</p>
        : erro ? <div role="alert" className="mt-3">
          <p>Não foi possível carregar suas preferências e categorias.</p>
          <button type="button" onClick={tentarNovamente} className="mt-3 rounded-full bg-floresta px-5 py-3 font-bold text-white hover:bg-folha">Tentar novamente</button>
        </div>
          : categoriasDisponiveis.length === 0 ? <p className="mt-3 text-slate-600">Ainda não há categorias disponíveis.</p>
            : <>
              {preferencias.length === 0 && <p className="mt-3 text-slate-600">Você ainda não selecionou nenhuma categoria.</p>}
              <ul className="mt-3 grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
                {categoriasDisponiveis.map((categoria) => {
                  const selecionada = preferencias.some((preferencia) => preferencia.categoriaAtrativoId === categoria.id)
                  const ocupada = Boolean(atualizando[categoria.id])
                  return (
                    <li key={categoria.id} className="min-w-0">
                      <button type="button" aria-pressed={selecionada} aria-busy={ocupada} disabled={ocupada} onClick={() => alternar(categoria.id, selecionada)} className={`flex min-h-14 w-full items-center gap-3 rounded-xl border p-3 text-left font-semibold transition disabled:cursor-wait disabled:opacity-60 ${selecionada ? 'border-floresta bg-floresta text-white hover:bg-folha' : 'border-slate-300 text-floresta hover:bg-creme'}`}>
                        {ocupada ? <LoaderCircle aria-hidden="true" className="size-5 shrink-0 animate-spin" /> : selecionada ? <Check aria-hidden="true" className="size-5 shrink-0" /> : <Plus aria-hidden="true" className="size-5 shrink-0" />}
                        <span className="min-w-0 [overflow-wrap:anywhere]">{categoria.nome}<span className="block text-xs font-normal">{ocupada ? 'Atualizando...' : !categoria.ativo ? 'Indisponível · remover preferência' : selecionada ? 'Selecionada' : 'Adicionar preferência'}</span></span>
                      </button>
                      {errosAtualizacao[categoria.id] && <p role="alert" className="mt-2 text-sm text-red-700">Não foi possível atualizar. Tente novamente.</p>}
                    </li>
                  )
                })}
              </ul>
            </>}
    </section>
  )
}

export default Preferencias
