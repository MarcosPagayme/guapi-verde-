import { useEffect, useRef, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { LoaderCircle } from 'lucide-react'
import { obterAtrativoPorId } from '../../../../servicos/atrativoService'
import { atualizarHorarioAtrativo, cadastrarHorarioAtrativo, excluirHorarioAtrativo, listarHorariosDoAtrativo } from '../../../../servicos/horarioFuncionamentoService'
import { diasSemana, nomeDia, ordenarHorarios } from './diasSemana'
import FormularioHorario from './FormularioHorario'
import ExcluirHorario from './ExcluirHorario'

const foco = 'focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta'
const botao = `inline-flex min-h-12 items-center justify-center rounded-full border border-floresta px-5 py-3 font-semibold text-floresta hover:bg-creme disabled:bg-slate-100 disabled:text-slate-600 ${foco}`

export default function GerenciarHorariosAtrativo() {
  const { atrativoId } = useParams()
  return <PaginaHorarios key={atrativoId} atrativoId={atrativoId} />
}

function PaginaHorarios({ atrativoId }) {
  const [dados, setDados] = useState(null)
  const [estado, setEstado] = useState('carregando')
  const [erro, setErro] = useState('')
  const [tentativa, setTentativa] = useState(0)
  const [acao, setAcao] = useState(null)
  const [mensagem, setMensagem] = useState('')
  const titulo = useRef(null)

  useEffect(() => {
    let ativo = true
    async function carregar() {
      try {
        const [atrativo, horarios] = await Promise.all([obterAtrativoPorId(atrativoId), listarHorariosDoAtrativo(atrativoId)])
        if (ativo) { setDados({ atrativo, horarios: ordenarHorarios(horarios) }); setEstado('pronto') }
      } catch (falha) {
        if (ativo) {
          setErro(falha.response?.status === 404 ? 'Atrativo não encontrado ou desativado.' : falha.response?.status === 403 ? 'Sua conta não possui permissão para acessar estes horários.' : 'Não foi possível carregar os horários do atrativo. Tente novamente.')
          setEstado('erro')
        }
      }
    }
    const inicio = setTimeout(carregar, 0)
    return () => { ativo = false; clearTimeout(inicio) }
  }, [atrativoId, tentativa])

  function recarregar() { setEstado('carregando'); setTentativa((valor) => valor + 1) }
  function abrir(tipo, horario, origem) { setMensagem(''); setAcao({ tipo, horario, origem }) }
  function concluir(texto) {
    setAcao(null)
    setMensagem(texto)
    recarregar()
  }
  async function salvar(payload) {
    if (acao.horario) await atualizarHorarioAtrativo(acao.horario.id, payload)
    else await cadastrarHorarioAtrativo(payload)
    concluir(acao.horario ? 'Horário atualizado com sucesso.' : 'Horário cadastrado com sucesso.')
  }
  async function excluir(id) {
    await excluirHorarioAtrativo(id)
    concluir('Horário excluído com sucesso.')
  }

  const todosConfigurados = dados && diasSemana.every(([dia]) => dados.horarios.some((item) => item.diaSemana === dia))

  return <section className="space-y-6 px-4 py-6 sm:px-6 sm:py-8" aria-labelledby="titulo-horarios">
    <Link to="/admin/atrativos" className={`inline-flex min-h-12 items-center rounded-lg font-semibold text-floresta ${foco}`}>← Voltar aos atrativos</Link>
    <div className="rounded-2xl border border-floresta/10 bg-white p-5 sm:p-7">
      <h1 ref={titulo} tabIndex={-1} id="titulo-horarios" className={`text-2xl font-bold text-floresta sm:text-3xl ${foco}`}>Horários de funcionamento</h1>
      {dados && <p className="mt-3 text-lg text-slate-700">{dados.atrativo.nome}</p>}
      {estado === 'pronto' && <>
        <p className="mt-3 text-slate-600">Dias cadastrados: {diasSemana.filter(([dia]) => dados.horarios.some((item) => item.diaSemana === dia)).length}</p>
        <button type="button" disabled={todosConfigurados} aria-describedby={todosConfigurados ? "dias-configurados" : undefined} onClick={(evento) => abrir('formulario', null, evento.currentTarget)} className={`mt-4 ${botao}`}>Adicionar horário</button>
        {todosConfigurados && <p id="dias-configurados" className="mt-3 text-sm text-slate-600">Todos os dias já foram configurados. Você pode editar os horários existentes.</p>}
      </>}
    </div>
    {mensagem && <p role="status" className="rounded-xl bg-white p-4 font-semibold text-floresta">{mensagem}</p>}
    {estado === 'carregando' && <p role="status" className="flex items-center gap-3 rounded-xl bg-white p-5 text-floresta"><LoaderCircle aria-hidden="true" className="size-5 animate-spin motion-reduce:animate-none" />Carregando horários…</p>}
    {estado === 'erro' && <div className="rounded-xl bg-white p-5"><p role="alert">{erro}</p><button type="button" onClick={recarregar} className={`mt-4 ${botao}`}>Tentar novamente</button></div>}
    {estado === 'pronto' && (dados.horarios.length === 0 ? <p role="status" className="rounded-xl bg-white p-5">Nenhum horário cadastrado para este atrativo.</p> : <ul aria-label="Horários cadastrados" className="grid gap-5 md:grid-cols-2">
      {dados.horarios.map((horario) => <li key={horario.id} className="flex min-w-0 flex-col rounded-2xl border border-floresta/10 bg-white p-5 shadow-sm">
        <h2 className="text-xl font-bold text-floresta">{nomeDia(horario.diaSemana)}</h2>
        <p className="mt-3 font-semibold text-slate-700">{horario.fechado ? 'Fechado' : `${horario.horarioAbertura?.slice(0, 5) ?? 'Não informado'} às ${horario.horarioFechamento?.slice(0, 5) ?? 'Não informado'}`}</p>
        {horario.observacao && <p className="mt-3 whitespace-pre-line text-slate-600 [overflow-wrap:anywhere]">{horario.observacao}</p>}
        <div className="mt-auto grid gap-3 pt-5 sm:grid-cols-2">
          <button type="button" aria-label={`Editar horário: ${nomeDia(horario.diaSemana)}`} onClick={(evento) => abrir('formulario', horario, evento.currentTarget)} className={botao}>Editar</button>
          <button type="button" aria-label={`Excluir horário: ${nomeDia(horario.diaSemana)}`} onClick={(evento) => abrir('excluir', horario, evento.currentTarget)} className={`min-h-12 rounded-full border border-red-800 px-5 py-3 font-semibold text-red-800 hover:bg-red-50 ${foco}`}>Excluir</button>
        </div>
      </li>)}
    </ul>)}
    {acao?.tipo === 'formulario' && <FormularioHorario horarios={dados?.horarios ?? []} sincronizando={estado !== 'pronto'} atrativoId={atrativoId} horario={acao.horario} origem={acao.origem} focoAlternativo={titulo} onCancelar={() => setAcao(null)} onSalvar={salvar} onSincronizar={recarregar} />}
    {acao?.tipo === 'excluir' && <ExcluirHorario horario={acao.horario} origem={acao.origem} focoAlternativo={titulo} onCancelar={() => setAcao(null)} onExcluir={excluir} onSincronizar={recarregar} />}
  </section>
}
