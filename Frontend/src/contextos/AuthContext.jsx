import { useEffect, useRef, useState } from 'react'
import { AuthContext } from '../hooks/useAuth'
import { buscarUsuarioAutenticado, realizarLogin } from '../servicos/authService'
import { lerSessao, normalizarSessao, observarRemocaoSessao, removerSessao, salvarSessao } from '../servicos/armazenamentoAuth'

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null)
  const [carregandoInicial, setCarregandoInicial] = useState(true)
  const versao = useRef(0)

  useEffect(() => {
    const controller = new AbortController()
    const versaoInicial = versao.current
    const pararObservacao = observarRemocaoSessao(() => {
      versao.current += 1
      setUsuario(null)
      setCarregandoInicial(false)
    })
    const sessao = lerSessao()
    async function restaurar() {
      try {
        if (!sessao) return
        const dados = await buscarUsuarioAutenticado(controller.signal)
        if (controller.signal.aborted || versao.current !== versaoInicial || lerSessao()?.token !== sessao.token) return
        const atualizada = normalizarSessao({ ...sessao, usuarioId: dados?.Id, nome: dados?.nome, email: dados?.email, perfil: dados?.perfil })
        if (!atualizada || dados.ativo !== true) {
          removerSessao(sessao.token)
          return
        }
        salvarSessao(atualizada)
        const { usuarioId, nome, email, perfil } = atualizada
        setUsuario({ usuarioId, nome, email, perfil })
      } catch (erro) {
        if (!controller.signal.aborted && [401, 403, 404].includes(erro?.response?.status)) removerSessao(sessao?.token)
        // Falhas de rede não autenticam o usuário nem apagam uma sessão potencialmente válida.
      } finally {
        if (!controller.signal.aborted) setCarregandoInicial(false)
      }
    }
    restaurar()
    return () => { controller.abort(); pararObservacao() }
  }, [])

  async function login(credenciais) {
    const tentativa = ++versao.current
    const dados = await realizarLogin(credenciais)
    if (tentativa !== versao.current) throw new Error('Autenticação cancelada.')
    const sessao = salvarSessao(dados)
    const { usuarioId, nome, email, perfil } = sessao
    const autenticado = { usuarioId, nome, email, perfil }
    setUsuario(autenticado)
    setCarregandoInicial(false)
    return autenticado
  }

  function logout() {
    removerSessao()
  }

  return <AuthContext.Provider value={{ usuario, autenticado: Boolean(usuario), carregandoInicial, login, logout }}>{children}</AuthContext.Provider>
}
