import api from './api'

export async function listarImagensDoAtrativo(atrativoId) {
  const resposta = await api.get(`/api/imagens-atrativos/atrativo/${encodeURIComponent(atrativoId)}`)
  return resposta.data
}

export async function cadastrarImagemAtrativo(dados) {
  const resposta = await api.post('/api/imagens-atrativos', dados)
  return resposta.data
}

export async function atualizarImagemAtrativo(id, dados) {
  const resposta = await api.put(`/api/imagens-atrativos/${encodeURIComponent(id)}`, dados)
  return resposta.data
}

export async function excluirImagemAtrativo(id) {
  const resposta = await api.delete(`/api/imagens-atrativos/${encodeURIComponent(id)}`)
  if (resposta.status !== 204) throw new Error('Não foi possível confirmar a exclusão.')
}
