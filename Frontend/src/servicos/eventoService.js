import api from './api'

export async function listarEventos() {
  const resposta = await api.get('/api/eventos')
  return resposta.data
}
