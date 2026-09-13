export const diasSemana = [
  ['SEGUNDA', 'Segunda-feira'],
  ['TERCA', 'Terça-feira'],
  ['QUARTA', 'Quarta-feira'],
  ['QUINTA', 'Quinta-feira'],
  ['SEXTA', 'Sexta-feira'],
  ['SABADO', 'Sábado'],
  ['DOMINGO', 'Domingo'],
]

export function nomeDia(valor) {
  return diasSemana.find(([dia]) => dia === valor)?.[1] ?? 'Dia não reconhecido'
}

export function ordenarHorarios(horarios) {
  const indice = (dia) => {
    const posicao = diasSemana.findIndex(([valor]) => valor === dia)
    return posicao < 0 ? 7 : posicao
  }
  return [...horarios].sort((a, b) => indice(a.diaSemana) - indice(b.diaSemana) || a.id - b.id)
}

export function validarHorario(valores, horarios, idAtual) {
  const erros = {}
  if (!diasSemana.some(([dia]) => dia === valores.diaSemana)) erros.diaSemana = 'Selecione um dia da semana.'
  else if (horarios.some((item) => item.id !== idAtual && item.diaSemana === valores.diaSemana)) erros.diaSemana = 'Este dia já possui um horário cadastrado.'
  if (!valores.fechado) {
    const formato = /^([01]\d|2[0-3]):[0-5]\d$/
    if (!formato.test(valores.horarioAbertura)) erros.horarioAbertura = 'Informe o horário de abertura.'
    if (!formato.test(valores.horarioFechamento)) erros.horarioFechamento = 'Informe o horário de fechamento.'
    if (!erros.horarioAbertura && !erros.horarioFechamento && valores.horarioAbertura >= valores.horarioFechamento) erros.horarioFechamento = 'O fechamento deve ser posterior à abertura, no mesmo dia.'
  }
  if (valores.observacao.length > 255) erros.observacao = 'Use no máximo 255 caracteres.'
  return erros
}
