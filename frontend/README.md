# Social Media Frontend

This is the frontend application for the social media platform, built with Next.js, TypeScript, and Tailwind CSS.

## Features

- User authentication (login/register)
- Create and view posts
- Like posts
- View user profiles
- Follow/unfollow users
- Modern, responsive UI

## Getting Started

1. Install dependencies:
```bash
npm install
```

2. Create a `.env.local` file in the root directory with the following content:
```
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

3. Run the development server:
```bash
npm run dev
```

4. Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

## Technologies Used

- Next.js 14
- TypeScript
- Tailwind CSS
- Zustand (State Management)
- Axios (API Client)
- React Hot Toast (Notifications)
- Heroicons (Icons)

## Project Structure

- `/app` - Next.js app router pages and layouts
- `/components` - Reusable React components
- `/store` - Zustand store configurations
- `/public` - Static assets

## Development

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run start` - Start production server
- `npm run lint` - Run ESLint 