import { useRef, useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { Eye, EyeOff, Leaf, LoaderCircle } from 'lucide-react'
import { useAuth } from '../../hooks/useAuth'
import { mensagemErroAuth } from '../../servicos/authService'

function Login() {
  const { login, carregandoInicial } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [mostrarSenha, setMostrarSenha] = useState(false)
  const [enviando, setEnviando] = useState(false)
  const [erro, setErro] = useState('')
  const envioEmCurso = useRef(false)

  async function enviar(evento) {
    evento.preventDefault()
    if (envioEmCurso.current || carregandoInicial) return
    const campos = new FormData(evento.currentTarget)
    const email = campos.get('email').trim()
    const senha = campos.get('senha')
    if (!email || !senha.trim()) {
      setErro('Informe seu e-mail e sua senha.')
      return
    }
    envioEmCurso.current = true
    setEnviando(true)
    setErro('')
    try {
      const usuario = await login({ email, senha })
      navigate(usuario.perfil === 'ADMIN' ? '/admin' : '/perfil', { replace: true })
    } catch (falha) {
      setErro(mensagemErroAuth(falha))
    } finally {
      envioEmCurso.current = false
      setEnviando(false)
    }
  }

  return (
    <section className="mx-auto max-w-md px-4 py-8 sm:py-12" aria-labelledby="titulo-login">
      <div className="rounded-3xl border border-floresta/10 bg-white p-6 shadow-sm sm:p-8">
        <Leaf className="mb-4 text-folha" size={32} aria-hidden="true" />
        <p className="text-sm font-semibold text-folha">Guapi Verde</p>
        <h1 id="titulo-login" className="mt-2 text-3xl font-bold text-floresta">Entre na sua conta</h1>
        <p className="mt-3 text-sm text-floresta/75">Seu próximo encontro com Guapimirim começa aqui.</p>
        {location.state?.cadastroRealizado === true && <p role="status" className="mt-5 rounded-xl bg-folha/10 p-3 text-sm text-floresta">Cadastro realizado! Você já pode entrar com seu e-mail e senha.</p>}
        {erro && <p id="erro-login" role="alert" className="mt-5 rounded-xl bg-red-50 p-3 text-sm text-red-800">{erro}</p>}
        <form onSubmit={enviar} className="mt-6 space-y-5" aria-busy={enviando} aria-describedby={erro ? 'erro-login' : undefined}>
          <fieldset disabled={enviando || carregandoInicial} className="space-y-5 disabled:opacity-70">
            <legend className="sr-only">Dados de acesso</legend>
            <div>
              <label htmlFor="email-login" className="mb-2 block text-sm font-semibold">E-mail</label>
              <input id="email-login" name="email" type="email" autoComplete="username" autoCapitalize="none" spellCheck={false} required className="w-full rounded-xl border border-floresta/25 px-4 py-3" />
            </div>
            <div>
              <label htmlFor="senha-login" className="mb-2 block text-sm font-semibold">Senha</label>
              <div className="relative">
                <input id="senha-login" name="senha" type={mostrarSenha ? 'text' : 'password'} autoComplete="current-password" required className="w-full rounded-xl border border-floresta/25 py-3 pl-4 pr-14" />
                <button type="button" onClick={() => setMostrarSenha(!mostrarSenha)} aria-label={mostrarSenha ? 'Ocultar senha' : 'Mostrar senha'} aria-pressed={mostrarSenha} aria-controls="senha-login" className="absolute inset-y-0 right-0 flex w-12 items-center justify-center rounded-xl text-floresta">
                  {mostrarSenha ? <EyeOff size={20} aria-hidden="true" /> : <Eye size={20} aria-hidden="true" />}
                </button>
              </div>
            </div>
            <button type="submit" className="flex min-h-12 w-full items-center justify-center gap-2 rounded-xl bg-floresta px-4 py-3 font-semibold text-white hover:bg-folha disabled:cursor-wait">
              {enviando && <LoaderCircle className="animate-spin" size={20} aria-hidden="true" />}
              {enviando ? 'Entrando…' : carregandoInicial ? 'Validando sessão…' : 'Entrar'}
            </button>
          </fieldset>
        </form>
        <p className="mt-6 text-center text-sm">Ainda não tem conta? <Link to="/cadastro" className="font-semibold text-floresta underline underline-offset-4">Criar conta</Link></p>
      </div>
    </section>
  )
}

export default Login
