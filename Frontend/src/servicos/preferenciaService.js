import api from './api'

export async function listarPreferencias(signal) {
  const resposta = await api.get('/api/preferencias', { signal })
  return resposta.data
}

export async function adicionarPreferencia(categoriaAtrativoId) {
  const resposta = await api.post(`/api/preferencias/${categoriaAtrativoId}`)
  return resposta.data
}

export async function removerPreferencia(categoriaAtrativoId) {
  const resposta = await api.delete(`/api/preferencias/${categoriaAtrativoId}`)
  return resposta.data
}
