import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import { defineConfig } from 'vite'
import { VitePWA } from 'vite-plugin-pwa'

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['icone-guapi-verde.svg'],
      manifest: {
        name: 'Guapi Verde',
        short_name: 'Guapi Verde',
        description:
          'Descubra o turismo ecológico, os atrativos e os eventos de Guapimirim.',
        lang: 'pt-BR',
        start_url: '/',
        display: 'standalone',
        theme_color: '#174D36',
        background_color: '#F7F3E8',
        icons: [
          {
            src: '/icone-guapi-verde.svg',
            sizes: 'any',
            type: 'image/svg+xml',
            purpose: 'any maskable',
          },
        ],
      },
    }),
  ],
})
