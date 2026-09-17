import api from './api'

export async function listarTemporadas() {
  const resposta = await api.get('/api/temporadas')
  return resposta.data
}