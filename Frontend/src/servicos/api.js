import axios from 'axios'
import { lerSessao, removerSessao } from './armazenamentoAuth'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use((config) => {
  const sessao = lerSessao()
  if (sessao && !config.semAutenticacao) {
    config.headers.set('Authorization', `Bearer ${sessao.token}`)
    config.tokenSessao = sessao.token
  }
  return config
})

api.interceptors.response.use((response) => response, (erro) => {
  if (erro.response?.status === 401 && erro.config?.tokenSessao) {
    removerSessao(erro.config.tokenSessao)
  }
  return Promise.reject(erro)
})

export default api
