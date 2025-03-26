'use client'

import { Inter } from 'next/font/google'
import { Toaster } from 'react-hot-toast'
import { usePathname } from 'next/navigation'

const inter = Inter({ subsets: ['latin'] })

export default function Providers({
  children,
}: {
  children: React.ReactNode
}) {
  const pathname = usePathname()
  const isAuthPage = pathname === '/login' || pathname === '/register'

  return (
    <div className={`${inter.className} h-full`}>
      <div className={`min-h-screen ${isAuthPage ? 'bg-gray-50' : 'bg-gray-100'}`}>
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 3000,
            style: {
              background: '#333',
              color: '#fff',
              borderRadius: '8px',
            },
            success: {
              style: {
                background: '#059669',
              },
            },
            error: {
              style: {
                background: '#dc2626',
              },
            },
          }}
        />
        <main className="min-h-screen">
          {children}
        </main>
      </div>
    </div>
  )
} 