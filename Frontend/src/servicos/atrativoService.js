import api from './api'

export async function listarAtrativos() {
  const resposta = await api.get('/api/atrativos')
  return resposta.data
}
