import { useEffect, useRef, useState } from 'react'
import { Heart, LoaderCircle } from 'lucide-react'
import { Link } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import { adicionarFavorito, listarFavoritos, removerFavorito } from '../../servicos/favoritoService'

const estilo = 'inline-flex min-h-11 w-full items-center justify-center gap-2 rounded-full border border-floresta px-5 py-3 text-sm font-bold transition focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-floresta disabled:cursor-wait disabled:opacity-60 sm:w-auto'

function FavoritoAutenticado({ atrativoId }) {
  const [favoritado, setFavoritado] = useState(false)
  const [consultando, setConsultando] = useState(true)
  const [salvando, setSalvando] = useState(false)
  const [erroConsulta, setErroConsulta] = useState(false)
  const [mensagem, setMensagem] = useState('')
  const [tentativa, setTentativa] = useState(0)
  const operacao = useRef(false)
  const consulta = useRef(null)

  useEffect(() => {
    const controller = new AbortController()
    consulta.current = controller
    const inicio = setTimeout(async () => {
      try {
        const favoritos = await listarFavoritos(controller.signal)
        if (!controller.signal.aborted) {
          setFavoritado(favoritos.some((favorito) => String(favorito.atrativoId) === String(atrativoId)))
        }
      } catch {
        if (!controller.signal.aborted) {
          setErroConsulta(true)
          setMensagem('Não foi possível consultar seus favoritos. Tente novamente.')
        }
      } finally {
        if (!controller.signal.aborted) setConsultando(false)
      }
    }, 0)
    return () => {
      clearTimeout(inicio)
      controller.abort()
    }
  }, [atrativoId, tentativa])

  async function alternar() {
    if (operacao.current || consultando) return
    setMensagem('')
    if (erroConsulta) {
      setErroConsulta(false)
      setConsultando(true)
      setTentativa((valor) => valor + 1)
      return
    }
    operacao.current = true
    setSalvando(true)
    const controller = consulta.current
    try {
      if (favoritado) await removerFavorito(atrativoId)
      else await adicionarFavorito(atrativoId)
      if (!controller.signal.aborted) {
        setFavoritado(!favoritado)
        setMensagem(favoritado ? 'Atrativo removido dos favoritos.' : 'Atrativo adicionado aos favoritos.')
      }
    } catch (erro) {
      if (controller.signal.aborted) return
      if (erro.response?.status === 409) {
        try {
          const favoritos = await listarFavoritos(controller.signal)
          if (!controller.signal.aborted) {
            setFavoritado(favoritos.some((favorito) => String(favorito.atrativoId) === String(atrativoId)))
            setMensagem('Seus favoritos foram atualizados.')
          }
        } catch {
          if (!controller.signal.aborted) {
            setErroConsulta(true)
            setMensagem('Não foi possível consultar seus favoritos. Tente novamente.')
          }
        }
      } else {
        setMensagem('Não foi possível alterar seu favorito. Tente novamente.')
      }
    } finally {
      operacao.current = false
      if (!controller.signal.aborted) setSalvando(false)
    }
  }

  return (
    <div className="mt-4">
      <button type="button" onClick={alternar} disabled={consultando || salvando}
        aria-pressed={favoritado} aria-busy={consultando || salvando}
        className={`${estilo} ${favoritado ? 'bg-floresta text-white hover:bg-folha' : 'bg-white text-floresta hover:bg-creme'}`}>
        <Heart aria-hidden="true" className="size-5 shrink-0" fill={favoritado ? 'currentColor' : 'none'} />
        {(consultando || salvando) && <LoaderCircle aria-hidden="true" className="size-4 shrink-0 animate-spin" />}
        {consultando ? 'Consultando favoritos...' : salvando ? 'Salvando...' : erroConsulta ? 'Consultar favoritos novamente'
          : favoritado ? 'Remover dos favoritos' : 'Adicionar aos favoritos'}
      </button>
      <p role="status" className="mt-2 text-sm text-slate-600">{mensagem}</p>
    </div>
  )
}

export default function BotaoFavorito({ atrativoId }) {
  const { autenticado, carregandoInicial, usuario } = useAuth()
  if (carregandoInicial) return <p role="status" className="mt-4 text-sm text-slate-600">Verificando sessão...</p>
  if (!autenticado) return (
    <Link to="/login" className={`${estilo} mt-4 bg-white text-floresta hover:bg-creme`}>
      <Heart aria-hidden="true" className="size-5 shrink-0" />Entre para favoritar
    </Link>
  )
  // Remonta o estado pessoal ao trocar de usuário ou de atrativo.
  return <FavoritoAutenticado key={`${usuario.usuarioId}:${atrativoId}`} atrativoId={atrativoId} />
}
