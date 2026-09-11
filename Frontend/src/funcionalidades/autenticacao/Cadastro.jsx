import { useEffect, useRef, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Eye, EyeOff, Leaf, LoaderCircle } from 'lucide-react'
import { cadastrarVisitante, mensagemErroAuth } from '../../servicos/authService'

function Cadastro() {
  const navigate = useNavigate()
  const [mostrarSenha, setMostrarSenha] = useState(false)
  const [enviando, setEnviando] = useState(false)
  const [erro, setErro] = useState('')
  const envioEmCurso = useRef(false)
  const montado = useRef(false)

  useEffect(() => {
    montado.current = true
    return () => { montado.current = false }
  }, [])

  async function enviar(evento) {
    evento.preventDefault()
    if (envioEmCurso.current) return
    const campos = new FormData(evento.currentTarget)
    const nome = campos.get('nome').trim()
    const email = campos.get('email').trim()
    const senha = campos.get('senha')
    const confirmarSenha = campos.get('confirmarSenha')
    if (!nome || nome.length > 100) { setErro('Informe um nome com até 100 caracteres.'); return }
    if (!email || email.length > 100 || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) { setErro('Informe um e-mail válido com até 100 caracteres.'); return }
    if (!senha.trim() || senha.length < 8 || senha.length > 72) { setErro('A senha deve ter entre 8 e 72 caracteres.'); return }
    if (senha !== confirmarSenha) { setErro('A confirmação precisa ser igual à senha.'); return }
    envioEmCurso.current = true
    setEnviando(true)
    setErro('')
    try {
      await cadastrarVisitante({ nome, email, senha })
      if (!montado.current) return
      navigate('/login', { replace: true, state: { cadastroRealizado: true } })
    } catch (falha) {
      if (montado.current) setErro(mensagemErroAuth(falha, 'cadastro'))
    } finally {
      envioEmCurso.current = false
      if (montado.current) setEnviando(false)
    }
  }

  return (
    <section className="mx-auto max-w-md px-4 py-8 sm:py-12" aria-labelledby="titulo-cadastro">
      <div className="rounded-3xl border border-floresta/10 bg-white p-6 shadow-sm sm:p-8">
        <Leaf className="mb-4 text-folha" size={32} aria-hidden="true" />
        <p className="text-sm font-semibold text-folha">Guapi Verde</p>
        <h1 id="titulo-cadastro" className="mt-2 text-3xl font-bold text-floresta">Crie sua conta</h1>
        <p className="mt-3 text-sm text-floresta/75">Faça parte e descubra mais de Guapimirim.</p>
        {erro && <p id="erro-cadastro" role="alert" className="mt-5 rounded-xl bg-red-50 p-3 text-sm text-red-800">{erro}</p>}
        <form onSubmit={enviar} className="mt-6 space-y-5" aria-busy={enviando} aria-describedby={erro ? 'erro-cadastro' : undefined}>
          <fieldset disabled={enviando} className="space-y-5 disabled:opacity-70">
            <legend className="sr-only">Dados para criar sua conta</legend>
            <div>
              <label htmlFor="nome-cadastro" className="mb-2 block text-sm font-semibold">Nome</label>
              <input id="nome-cadastro" name="nome" autoComplete="name" required maxLength={100} className="w-full rounded-xl border border-floresta/25 px-4 py-3" />
            </div>
            <div>
              <label htmlFor="email-cadastro" className="mb-2 block text-sm font-semibold">E-mail</label>
              <input id="email-cadastro" name="email" type="email" autoComplete="email" autoCapitalize="none" spellCheck={false} required maxLength={100} className="w-full rounded-xl border border-floresta/25 px-4 py-3" />
            </div>
            <p id="ajuda-senha" className="text-sm text-floresta/75">Use uma senha com 8 a 72 caracteres.</p>
            {[
              { nome: 'senha', rotulo: 'Senha' },
              { nome: 'confirmarSenha', rotulo: 'Confirmar senha' },
            ].map((campo) => (
              <div key={campo.nome}>
                <label htmlFor={`${campo.nome}-cadastro`} className="mb-2 block text-sm font-semibold">{campo.rotulo}</label>
                <input id={`${campo.nome}-cadastro`} name={campo.nome} type={mostrarSenha ? 'text' : 'password'} autoComplete="new-password" required minLength={8} maxLength={72} aria-describedby="ajuda-senha" className="w-full rounded-xl border border-floresta/25 px-4 py-3" />
              </div>
            ))}
            <button type="button" onClick={() => setMostrarSenha(!mostrarSenha)} aria-pressed={mostrarSenha} aria-controls="senha-cadastro confirmarSenha-cadastro" className="flex min-h-11 items-center gap-2 rounded-lg text-sm font-semibold text-floresta">
              {mostrarSenha ? <EyeOff size={20} aria-hidden="true" /> : <Eye size={20} aria-hidden="true" />}
              {mostrarSenha ? 'Ocultar senhas' : 'Mostrar senhas'}
            </button>
            <button type="submit" className="flex min-h-12 w-full items-center justify-center gap-2 rounded-xl bg-floresta px-4 py-3 font-semibold text-white hover:bg-folha disabled:cursor-wait">
              {enviando && <LoaderCircle className="animate-spin" size={20} aria-hidden="true" />}
              {enviando ? 'Criando conta…' : 'Criar conta'}
            </button>
          </fieldset>
        </form>
        <p className="mt-6 text-center text-sm">Já tem conta? <Link to="/login" className="font-semibold text-floresta underline underline-offset-4">Entrar</Link></p>
      </div>
    </section>
  )
}

export default Cadastro
