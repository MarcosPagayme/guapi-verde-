import api from './api'

export async function listarImagensDoAtrativo(atrativoId) {
  const resposta = await api.get(`/api/imagens-atrativos/atrativo/${atrativoId}`)
  return resposta.data
}
