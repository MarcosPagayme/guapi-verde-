import api from './api'

export async function cadastrarAtrativo(dados) {
  const resposta = await api.post('/api/atrativos', dados)
  return resposta.data
}

export async function atualizarAtrativo(id, dados) {
  const resposta = await api.put(`/api/atrativos/${encodeURIComponent(id)}`, dados)
  return resposta.data
}

export async function desativarAtrativo(id) {
  const resposta = await api.delete(`/api/atrativos/${encodeURIComponent(id)}`)
  if (resposta.status !== 204) throw new Error('Não foi possível confirmar a desativação.')
}

export async function listarAtrativos() {
  const resposta = await api.get('/api/atrativos')
  return resposta.data
}

export async function obterAtrativoPorId(id) {
  const resposta = await api.get(`/api/atrativos/${encodeURIComponent(id)}`)
  return resposta.data
}
