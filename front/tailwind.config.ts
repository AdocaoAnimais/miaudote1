import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      backgroundImage: {
        blur: 'url(/assets/blur-background.png)',
      },
      fontFamily: {
        sans: 'Roboto, sans-serif',
      },
      keyframes: {
        hide: {
          from: { opacity: '1' },
          to: { opacity: '0' },
        },
        slideIn: {
          from: { transform: 'translateX(calc(100% + var(--viewport-padding)))' },
          to: { transform: 'translateX(0)' },
        },
        swipeOut: {
          from: { transform: 'translateX(var(--radix-toast-swipe-end-x))' },
          to: { transform: 'translateX(calc(100% + var(--viewport-padding)))' },
        },
      },
      animation: {
        hide: 'hide 100ms ease-in',
        slideIn: 'slideIn 150ms cubic-bezier(0.16, 1, 0.3, 1)',
        swipeOut: 'swipeOut 100ms ease-out',
      },
      colors: {
        theme: {
          'primary': '#7AC7E3',
          'secondary': '#0EE7B7',

          'bg': '#0b132d',
          'bgtext': '#E0E1E3',
          'button1': '#f2a812',
          'button2': '#485B39',
          'border': '#f2a812',
          'inputbg': '#111333',
          
          'text': '#FFFFFF',
          'text2': '#737380',
          'texthighlight': '#f2a812',
          'title': '#000000',

          'confirm': '#FFA400',
          'error': '#d45d5d',
          'menu': '#DCDCDC',
          'gradient': 'linear-gradient(225deg, #171429 10%, #1a2037 90%);',
        },
        color: {
          'primary': '#7AC7E3',
          'secondary': '#0EE7B7',

          'bg': '#171429',
          'bgtext': '#E0E1E3',
          'button1': '#0E2A47',
          'button2': '#485B39',
          'border': '#313958',
          'inputbg': '#1E253E',
          
          'text': '#000000',
          'text2': '#000000',
          'texthighlight': '#444B5B',
          'title': '#000000',
          
          'confirm': '#FFA400',
          'error': '#d45d5d',
          'menu': '#DCDCDC',
          'gradient': 'linear-gradient(225deg, #171429 10%, #1a2037 90%);',
        }
      },
    },
  },
  plugins: [],
}
export default config
