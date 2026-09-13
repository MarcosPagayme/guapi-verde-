import api from './api'

export async function listarCategoriasAtrativos() {
  const categorias = await listarCategoriasAtrativosAdministracao()
  return categorias.filter((categoria) => categoria.ativo === true)
}

export async function listarCategoriasAtrativosAdministracao() {
  const resposta = await api.get('/api/categorias-atrativos')
  return resposta.data
}

export async function cadastrarCategoriaAtrativo(dados) {
  const resposta = await api.post('/api/categorias-atrativos', dados)
  return resposta.data
}

export async function atualizarCategoriaAtrativo(id, dados) {
  const resposta = await api.put(`/api/categorias-atrativos/${encodeURIComponent(id)}`, dados)
  return resposta.data
}

export async function desativarCategoriaAtrativo(id) {
  const resposta = await api.delete(`/api/categorias-atrativos/${encodeURIComponent(id)}`)
  if (resposta.status !== 204) throw new Error('Não foi possível confirmar a desativação.')
}
