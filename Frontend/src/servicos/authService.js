import api from './api'

const opcoesPublicas = { semAutenticacao: true, timeout: 15000 }

export async function realizarLogin({ email, senha }) {
  const { data } = await api.post('/api/auth/login', { email: email.trim(), senha }, opcoesPublicas)
  return data
}

export async function cadastrarVisitante({ nome, email, senha }) {
  const { data } = await api.post('/api/auth/cadastro', { nome: nome.trim(), email: email.trim(), senha }, opcoesPublicas)
  return data
}

export async function buscarUsuarioAutenticado(signal) {
  const { data } = await api.get('/api/auth/me', { signal, timeout: 15000 })
  return data
}

// Somente mensagens conhecidas do contrato podem ser exibidas diretamente.
const mensagensSeguras = new Set([
  'Já existe um usuário cadastrado com esse e-mail.',
  'O nome é obrigatório', 'O nome deve ter no máximo 100 caracteres',
  'O email é obrigatório', 'O email deve ser válido', 'O email deve ter no máximo 100 caracteres',
  'A senha é obrigatória', 'A senha deve ter entre 8 e 72 caracteres',
  'O campo email é obrigatório', 'O campo email deve ser um endereço de email válido',
  'O campo senha é obrigatório',
])

export function mensagemErroAuth(erro, operacao = 'login') {
  const status = erro?.response?.status
  if (!status || status >= 500) return 'Não foi possível conectar ao serviço. Tente novamente em instantes.'
  if (status === 401) return operacao === 'login' ? 'E-mail ou senha inválidos.' : 'Não foi possível criar sua conta. Tente novamente.'
  const dados = erro?.response?.data
  if (mensagensSeguras.has(dados?.mensagem)) return dados.mensagem
  if (status === 409 && operacao === 'cadastro') return 'Este e-mail já está cadastrado. Entre com sua conta.'
  if (status === 400) {
    const campos = dados?.campos
    const mensagens = campos && typeof campos === 'object'
      ? Object.values(campos).filter((valor) => mensagensSeguras.has(valor)) : []
    return mensagens.length ? [...new Set(mensagens)].join('. ') : 'Verifique os campos informados e tente novamente.'
  }
  return 'Não foi possível concluir a solicitação. Tente novamente.'
}
