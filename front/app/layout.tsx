import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import './globals.css' 
import { Footer } from './components/Footer'
import NavBar from './components/NavBar' 

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'Miaudote',
  description: 'Adoção de animais.', 
  icons: {
    icon: '/icon.ico',
  }
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="pt-BR">
        <body className={inter.className}>
          <NavBar />
          {children}
          <Footer />
        </body>
    </html>
  )
}
