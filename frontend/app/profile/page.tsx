'use client'

import { useEffect, useState } from 'react'
import { useAuthStore } from '../../store/auth'
import Navigation from '../../components/Navigation'
import axios from 'axios'
import toast from 'react-hot-toast'

import apiClient from './../../lib/axios';

interface Profile {
  id: number
  username: string
  email: string
  followers: number
  following: number
  posts: Array<{
    id: number
    content: string
    createdAt: string
    likes: number
  }>
}

export default function ProfilePage() {
  const [profile, setProfile] = useState<Profile | null>(null)
  const { user } = useAuthStore()

  useEffect(() => {
    if (user) {
      fetchProfile()
    }
  }, [user])

  const fetchProfile = async () => {
    try {
      const response = await apiClient.get(`http://localhost:8080/api/users/${user?.id}/profile`)
      setProfile(response.data)
    } catch (error) {
      toast.error('Failed to fetch profile')
    }
  }

  if (!profile) {
    return <div>Loading...</div>
  }

  return (
    <div className="flex min-h-screen bg-gray-100">
      <Navigation />
      
      <main className="flex-1 max-w-2xl mx-auto p-4">
        <div className="bg-white rounded-lg shadow p-6 mb-6">
          <div className="text-center">
            <h1 className="text-2xl font-bold mb-2">{profile.username}</h1>
            <p className="text-gray-600 mb-4">{profile.email}</p>
            
            <div className="flex justify-center space-x-4">
              <div>
                <span className="font-bold">{profile.followers}</span>
                <span className="text-gray-600 ml-1">Followers</span>
              </div>
              <div>
                <span className="font-bold">{profile.following}</span>
                <span className="text-gray-600 ml-1">Following</span>
              </div>
            </div>
          </div>
        </div>

        <div className="space-y-4">
          <h2 className="text-xl font-bold mb-4">Your Posts</h2>
          
          {profile.posts.map((post) => (
            <div key={post.id} className="bg-white p-4 rounded-lg shadow">
              <p className="text-gray-800 mb-2">{post.content}</p>
              
              <div className="flex items-center justify-between text-sm text-gray-500">
                <span>{new Date(post.createdAt).toLocaleDateString()}</span>
                <div className="flex items-center">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-5 w-5 mr-1"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
                    />
                  </svg>
                  <span>{post.likes}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </main>
    </div>
  )
} 