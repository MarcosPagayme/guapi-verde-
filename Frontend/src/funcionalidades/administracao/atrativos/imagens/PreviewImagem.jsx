import { useState } from 'react'
import { Leaf } from 'lucide-react'

export default function PreviewImagem({ url, textoAlternativo }) {
  return <Preview key={url} url={url} textoAlternativo={textoAlternativo} />
}

function Preview({ url, textoAlternativo }) {
  const [falhou, setFalhou] = useState(false)
  return falhou ? (
    <div role="img" aria-label={`Imagem indisponível: ${textoAlternativo}`} className="flex aspect-video items-center justify-center gap-3 rounded-xl bg-creme p-4 text-floresta">
      <Leaf aria-hidden="true" className="size-8 shrink-0" /> Prévia indisponível
    </div>
  ) : <img src={url} alt={textoAlternativo} onError={() => setFalhou(true)} className="aspect-video w-full rounded-xl bg-creme object-cover" />
}
