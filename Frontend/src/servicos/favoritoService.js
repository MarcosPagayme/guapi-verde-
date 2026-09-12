import api from './api'

export async function listarFavoritos(signal) {
  const resposta = await api.get('/api/favoritos', { signal })
  return resposta.data
}

export async function removerFavorito(atrativoId) {
  const resposta = await api.delete(`/api/favoritos/${atrativoId}`)
  return resposta.data
}

export async function adicionarFavorito(atrativoId) {
  const resposta = await api.post(`/api/favoritos/${atrativoId}`)
  return resposta.data
}
