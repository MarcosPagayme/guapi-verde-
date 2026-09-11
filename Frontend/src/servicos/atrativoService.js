import api from './api'

export async function listarAtrativos() {
  const resposta = await api.get('/api/atrativos')
  return resposta.data
}

export async function obterAtrativoPorId(id) {
  const resposta = await api.get(`/api/atrativos/${encodeURIComponent(id)}`)
  return resposta.data
}
