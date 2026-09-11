export const CHAVE_AUTENTICACAO = 'guapi_verde_autenticacao'
const EVENTO_SESSAO_REMOVIDA = 'guapi-verde:sessao-removida'

export function normalizarSessao(dados) {
  if (!dados || typeof dados.token !== 'string' || !dados.token.trim()
    || dados.tipo !== 'Bearer' || !Number.isSafeInteger(dados.usuarioId) || dados.usuarioId <= 0
    || typeof dados.nome !== 'string' || !dados.nome.trim()
    || typeof dados.email !== 'string' || !dados.email.trim()
    || !['ADMIN', 'VISITANTE'].includes(dados.perfil)) return null

  const { token, tipo, usuarioId, nome, email, perfil } = dados
  return { token, tipo, usuarioId, nome, email, perfil }
}

export function removerSessao(token) {
  if (token && lerSessao()?.token !== token) return
  try { localStorage.removeItem(CHAVE_AUTENTICACAO) } catch { /* Armazenamento indisponível. */ }
  window.dispatchEvent(new Event(EVENTO_SESSAO_REMOVIDA))
}

export function lerSessao() {
  try {
    const valor = localStorage.getItem(CHAVE_AUTENTICACAO)
    if (valor === null) return null
    const sessao = normalizarSessao(JSON.parse(valor))
    if (sessao) return sessao
  } catch { /* Dados corrompidos ou acesso bloqueado. */ }
  try { localStorage.removeItem(CHAVE_AUTENTICACAO) } catch { /* Sem acesso ao armazenamento. */ }
  return null
}

export function salvarSessao(dados) {
  const sessao = normalizarSessao(dados)
  if (!sessao) throw new Error('Resposta de autenticação inválida.')
  localStorage.setItem(CHAVE_AUTENTICACAO, JSON.stringify(sessao))
  return sessao
}

export function observarRemocaoSessao(callback) {
  const aoAlterarArmazenamento = (evento) => {
    if (evento.key === null) { callback(); return }
    if (evento.key !== CHAVE_AUTENTICACAO) return
    try {
      const anterior = normalizarSessao(JSON.parse(evento.oldValue))
      const atual = normalizarSessao(JSON.parse(evento.newValue))
      // A atualização de /me em outra aba não encerra a mesma sessão.
      if (anterior && atual && anterior.token === atual.token) return
    } catch { /* Sessão removida ou dados inválidos em outra aba. */ }
    callback()
  }
  window.addEventListener(EVENTO_SESSAO_REMOVIDA, callback)
  window.addEventListener('storage', aoAlterarArmazenamento)
  return () => {
    window.removeEventListener(EVENTO_SESSAO_REMOVIDA, callback)
    window.removeEventListener('storage', aoAlterarArmazenamento)
  }
}
