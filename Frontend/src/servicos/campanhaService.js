import api from './api'

export async function listarCampanhas() {
  const resposta = await api.get('/api/campanhas')
  return resposta.data
}