export function obterRotaPerfil(usuario) {
  return usuario?.perfil === 'ADMIN' ? '/admin' : '/perfil'
}
