import api from './api'

export async function listarHorariosDoAtrativo(atrativoId) {
  const resposta = await api.get(`/api/horarios-funcionamento/atrativo/${encodeURIComponent(atrativoId)}`)
  return resposta.data
}
