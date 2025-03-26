import Link from 'next/link'
import { useAuthStore } from '../store/auth'
import { usePathname } from 'next/navigation'
import {
  HomeIcon,
  UserIcon,
  BellIcon,
  PlusIcon,
  ArrowRightOnRectangleIcon
} from '@heroicons/react/24/outline'
import {
  HomeIcon as HomeIconSolid,
  UserIcon as UserIconSolid,
  BellIcon as BellIconSolid,
  PlusIcon as PlusIconSolid,
} from '@heroicons/react/24/solid'

import { useRouter } from 'next/navigation'



export default function Navigation() {
  const { isAuthenticated, user, logout } = useAuthStore()
  const pathname = usePathname()
  const router = useRouter()

  if (!isAuthenticated) return null

  const navigation = [
    { name: 'Home', href: '/', icon: HomeIcon, activeIcon: HomeIconSolid },
    { name: 'Profile', href: '/profile', icon: UserIcon, activeIcon: UserIconSolid },
    { name: 'Notifications', href: '/notifications', icon: BellIcon, activeIcon: BellIconSolid },
    { name: 'Create Post', href: '/create', icon: PlusIcon, activeIcon: PlusIconSolid },
  ]

  return (
    <nav className="fixed bottom-0 left-0 w-full bg-white border-t md:relative md:border-t-0 md:border-r md:w-64 md:h-screen z-50">
      <div className="flex justify-around md:flex-col md:justify-start md:p-6 md:space-y-2">
        {navigation.map((item) => {
          const isActive = pathname === item.href
          const Icon = isActive ? item.activeIcon : item.icon
          
          return (
            <Link
              key={item.name}
              href={item.href}
              className={`flex items-center p-3 md:p-4 rounded-lg transition-all duration-200 group ${
                isActive
                  ? 'text-primary-600 bg-primary-50 hover:bg-primary-100'
                  : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              <Icon className={`h-6 w-6 transition-colors duration-200 ${
                isActive ? 'text-primary-600' : 'text-gray-500 group-hover:text-gray-600'
              }`} />
              <span className={`hidden md:block ml-3 font-medium ${
                isActive ? 'text-primary-600' : 'text-gray-700'
              }`}>
                {item.name}
              </span>
            </Link>
          )
        })}

        <button
          onClick={() => {
            logout();                // your existing logout action from useAuthStore
            router.push('/login');   // explicitly redirect to login after logout
          }}
          className="flex items-center p-3 md:p-4 rounded-lg text-gray-600 hover:bg-gray-50 transition-all duration-200 group"
        >
          <ArrowRightOnRectangleIcon className="h-6 w-6 text-gray-500 group-hover:text-gray-600" />
          <span className="hidden md:block ml-3 font-medium text-gray-700">
            Logout
          </span>
        </button>
      </div>
    </nav>
  )
} 