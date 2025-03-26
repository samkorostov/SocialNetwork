'use client'

import { useEffect, useState } from 'react'
import { useAuthStore } from '../store/auth'
import Navigation from '../components/Navigation'
import apiClient from '../lib/axios'
import toast from 'react-hot-toast'
import { useRouter } from 'next/navigation'
import { HeartIcon, ChatBubbleLeftIcon, ShareIcon } from '@heroicons/react/24/outline'
import { HeartIcon as HeartIconSolid } from '@heroicons/react/24/solid'

interface Post {
  id: number
  content: string
  likes: number
  liked: boolean
  authorId: number
  authorName: string
  createdAt: string
}

export default function HomePage() {
  const [posts, setPosts] = useState<Post[]>([])
  const { isAuthenticated, initializeFromStorage } = useAuthStore()
  const router = useRouter()
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const init = async () => {
      await initializeFromStorage()
      if (!isAuthenticated) {
        router.push('/login')
        return
      }
      fetchPosts()
    }
    init()
  }, [])

  const fetchPosts = async () => {
    try {
      setIsLoading(true)
      const response = await apiClient.get('/posts')
      console.log('response', response.data)
      setPosts(response.data)
    } catch (error) {
      toast.error('Failed to fetch posts')
    } finally {
      setIsLoading(false)
    }
  }

  const handleLike = async (postId: number) => {
    try {
      const response = await apiClient.post(`/posts/${postId}/like`);
      const updatedPost = response.data;
      const currentUserId = useAuthStore.getState().userId; // Adjust based on your auth store
  
      setPosts(posts.map(post => {
        if (post.id === postId) {
          return {
            ...post,
            likes: updatedPost.likes,
            liked: updatedPost.liked,
          };
        }
        return post;
      }));
    } catch (error) {
      toast.error('Failed to like post');
    }
  };
  

  if (!isAuthenticated) return null

  return (
    <div className="flex min-h-screen bg-gray-50">
      <Navigation />
      
      <main className="flex-1 max-w-2xl mx-auto px-4 py-8">
        {isLoading ? (
          <div className="space-y-4">
            {[1, 2, 3].map((i) => (
              <div key={i} className="bg-white rounded-xl shadow p-6 animate-pulse">
                <div className="flex items-center space-x-4 mb-4">
                  <div className="h-12 w-12 bg-gray-200 rounded-full" />
                  <div className="flex-1 space-y-2">
                    <div className="h-4 bg-gray-200 rounded w-1/4" />
                    <div className="h-3 bg-gray-200 rounded w-1/6" />
                  </div>
                </div>
                <div className="space-y-2">
                  <div className="h-4 bg-gray-200 rounded w-3/4" />
                  <div className="h-4 bg-gray-200 rounded w-1/2" />
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="space-y-6">
            {posts.map((post) => (
              <div
                key={post.id}
                className="bg-white rounded-xl shadow-sm hover:shadow-md transition-shadow duration-200 overflow-hidden animate-fade-in"
              >
                <div className="p-6">
                  <div className="flex items-center mb-4">
                    <div className="h-12 w-12 bg-gradient-to-r from-purple-400 to-pink-500 rounded-full flex items-center justify-center text-white font-bold text-lg">
                      {post.authorName.charAt(0).toUpperCase()}
                    </div>
                    <div className="ml-4">
                      <h3 className="font-semibold text-gray-900">{post.authorName}</h3>
                      <p className="text-sm text-gray-500">
                        {new Date(post.createdAt).toLocaleDateString('en-US', {
                          month: 'short',
                          day: 'numeric',
                          hour: '2-digit',
                          minute: '2-digit'
                        })}
                      </p>
                    </div>
                  </div>
                  
                  <p className="text-gray-800 text-lg mb-4">{post.content}</p>
                  
                  <div className="flex items-center space-x-6 text-gray-500">
                    <button
                      onClick={() => handleLike(post.id)}
                      className={`flex items-center space-x-2 transition-colors duration-200 group ${
                        post.liked ? 'text-red-500' : 'hover:text-red-500'
                      }`}
                    >
                      {post.liked ? (
                        <HeartIconSolid className="h-6 w-6" />
                      ) : (
                        <HeartIcon className="h-6 w-6" />
                      )}
                      <span>{post.likes}</span>
                    </button>
                    
                    <button className="flex items-center space-x-2 hover:text-blue-500 transition-colors duration-200">
                      <ChatBubbleLeftIcon className="h-6 w-6" />
                      <span>Comment</span>
                    </button>
                    
                    <button className="flex items-center space-x-2 hover:text-green-500 transition-colors duration-200">
                      <ShareIcon className="h-6 w-6" />
                      <span>Share</span>
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  )
} 