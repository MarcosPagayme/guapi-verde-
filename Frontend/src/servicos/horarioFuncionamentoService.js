import api from './api'

export async function listarHorariosDoAtrativo(atrativoId) {
  const resposta = await api.get(`/api/horarios-funcionamento/atrativo/${encodeURIComponent(atrativoId)}`)
  return resposta.data
}

export async function cadastrarHorarioAtrativo(dados) {
  const resposta = await api.post('/api/horarios-funcionamento', dados)
  return resposta.data
}

export async function atualizarHorarioAtrativo(id, dados) {
  const resposta = await api.put(`/api/horarios-funcionamento/${encodeURIComponent(id)}`, dados)
  return resposta.data
}

export async function excluirHorarioAtrativo(id) {
  const resposta = await api.delete(`/api/horarios-funcionamento/${encodeURIComponent(id)}`)
  if (resposta.status !== 204) throw new Error('Não foi possível confirmar a exclusão.')
}
