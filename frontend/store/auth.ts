import { create } from 'zustand'
import apiClient from '../lib/axios'

interface User {
  id: number
  username: string
  email?: string
  bio?: string
  profilePictureUrl?: string
  followersCount?: number
  followingCount?: number
}

interface AuthState {
  user: User | null
  isAuthenticated: boolean
  initializeFromStorage: () => Promise<void>
  login: (username: string, password: string) => Promise<void>
  register: (username: string, email: string, password: string) => Promise<void>
  logout: () => void
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthenticated: false,

  initializeFromStorage: async () => {
    const token = localStorage.getItem('token');
    console.log('token', token)
    
    if (token) {
      try {
        const response = await apiClient.get('/auth/me');
        set({ user: response.data, isAuthenticated: true });
      } catch (error) {
        localStorage.removeItem('token');
        set({ user: null, isAuthenticated: false });
      }
    }
  },

  login: async (username: string, password: string) => {
    try {
      const response = await apiClient.post('/auth/login', {
        username,
        password,
      })
      const { token } = response.data
      localStorage.setItem('token', token)
      
      // Fetch user data after successful login
      const userResponse = await apiClient.get('/auth/me')
      set({ user: userResponse.data, isAuthenticated: true })
    } catch (error) {
      throw error
    }
  },

  register: async (username: string, email: string, password: string) => {
    try {
      const response = await apiClient.post('/auth/register', {
        username,
        email,
        password,
      })
      const { token } = response.data
      localStorage.setItem('token', token)
      
      // Fetch user data after successful registration
      const userResponse = await apiClient.get('/auth/me')
      set({ user: userResponse.data, isAuthenticated: true })
    } catch (error) {
      throw error
    }
  },

  logout: () => {
    localStorage.removeItem('token')
    set({ user: null, isAuthenticated: false })
  },
})) 