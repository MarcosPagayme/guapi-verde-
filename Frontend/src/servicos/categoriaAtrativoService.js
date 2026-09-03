import api from './api'

export async function listarCategoriasAtrativos() {
  const resposta = await api.get('/api/categorias-atrativos')
  return resposta.data
}
