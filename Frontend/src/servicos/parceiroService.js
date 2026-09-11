import api from './api'

export async function listarParceiros() {
  const resposta = await api.get('/api/parceiros')
  return resposta.data
}