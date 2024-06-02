

export default function ButtonsReceptacle() {

    const PrimaryButton = ({ text }: { text: string }) => (
        <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            {text}
        </button>
    );
    const SecondaryButton = ({ text }: { text: string }) => (
        <button className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded">
            {text}
        </button>
    );
    const OutlineButton = ({ text }: { text: string }) => (
        <button className="bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded">
            {text}
        </button>
    );
    const ShadowButton = ({ text }: { text: string }) => (
        <button className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded shadow-lg">
            {text}
        </button>
    );
    const GradientButton = ({ text }: { text: string }) => (
        <button className="bg-gradient-to-r from-purple-400 via-pink-500 to-red-500 hover:from-purple-500 hover:via-pink-600 hover:to-red-600 text-white font-bold py-2 px-4 rounded">
            {text}
        </button>
    );
    const AnimatedButton = ({ text }: { text: string }) => (
        <button className="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded transform hover:scale-105 transition-transform duration-200 ease-in-out">
            {text}
        </button>
    );
    const IconButton = ({ text }: { text: string }) => (
        <button className="bg-indigo-500 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded flex items-center">
            <svg className="w-6 h-6 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
            {text}
        </button>
    );
    const GlassButton = ({ text }: { text: string }) => (
        <button className="bg-white bg-opacity-20 backdrop-filter backdrop-blur-lg text-white font-bold py-2 px-4 rounded hover:bg-opacity-30">
            {text}
        </button>
    );
    const InnerShadowButton = ({ text }: { text: string }) => (
        <button className="bg-red-500 text-white font-bold py-2 px-4 rounded-lg shadow-inner">
            {text}
        </button>
    );
    const LeftIconAnimatedButton = ({ text }: { text: string }) => (
        <button className="bg-blue-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 transform transition duration-500 hover:scale-110">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
            <span>{text}</span>
        </button>
    );
    const RightIconSlideButton = ({ text }: { text: string }) => (
        <button className="bg-purple-500 text-white font-bold py-2 px-4 rounded flex items-center justify-between transform transition duration-500 hover:translate-x-2">
            <span>{text}</span>
            <svg className="w-6 h-6 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
        </button>
    );
    const PulseIconButton = ({ text }: { text: string }) => (
        <button className="bg-red-500 text-white font-bold py-2 px-4 rounded flex items-center justify-center space-x-2 animate-pulse">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
            <span>{text}</span>
        </button>
    );
    const LoadingIconButton = ({ text }: { text: string }) => (
        <button className="bg-green-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2">
            <svg className="w-6 h-6 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v4m0 0l-4-4m4 4l4-4m-4 12v4m0 0l4-4m-4 4l-4-4m8 8H8"></path></svg>
            <span>{text}</span>
        </button>
    );
    const BorderRotateIconButton = ({ text }: { text: string }) => (
        <button className="bg-yellow-500 text-white font-bold py-2 px-4 rounded-full flex items-center space-x-2 border-2 border-white hover:border-transparent hover:rotate-180 transition duration-500">
            <span>{text}</span>
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
        </button>
    );
    const ShineIconButton = ({ text }: { text: string }) => (
        <button className="bg-teal-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 relative overflow-hidden">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
            <span>{text}</span>
            <div className="absolute inset-0 bg-white opacity-20 transition duration-500 hover:opacity-0"></div>
        </button>
    );
    const PressIconButton = ({ text }: { text: string }) => (
        <button className="bg-pink-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 transform transition duration-200 active:scale-95">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
            <span>{text}</span>
        </button>
    );
    const FloatingIconButton = ({ text }: { text: string }) => (
        <button className="bg-gray-800 text-white font-bold py-2 px-4 rounded-full flex items-center space-x-2 transform transition duration-500 hover:translate-y-1">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
            <span>{text}</span>
        </button>
    );
    const ColorChangeIconButton = ({ text }: { text: string }) => (
        <button className="bg-indigo-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 transition duration-500 hover:bg-indigo-700">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
            <span>{text}</span>
        </button>
    );
    const GrowIconButton = ({ text }: { text: string }) => (
        <button className="bg-blue-600 text-white font-bold py-2 px-4 rounded-lg flex items-center space-x-2 transform transition duration-300 hover:scale-105">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
            <span>{text}</span>
        </button>
    );
    const PulseCheckButton = ({ text }: { text: string }) => (
        <button className="bg-green-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 animate-pulse">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
            <span>{text}</span>
        </button>
    );
    const BounceDownloadButton = ({ text }: { text: string }) => (
        <button className="bg-blue-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 transition-transform duration-200 hover:animate-bounce">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4"></path></svg>
            <span>{text}</span>
        </button>
    );
    const HeartBeatButton = ({ text }: { text: string }) => (
        <button className="bg-pink-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 animate-ping">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 21c4.418 0 8-3.582 8-8s-3.582-8-8-8-8 3.582-8 8 3.582 8 8 8z"></path></svg>
            <span>{text}</span>
        </button>
    );
    const ShineStarButton = ({ text }: { text: string }) => (
        <button className="bg-yellow-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 relative overflow-hidden">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 2l3.09 6.26L22 9.27l-5 4.87L17.18 22 12 18.27 6.82 22 8 14.14l-5-4.87 6.91-1.01L12 2z"></path></svg>
            <span>{text}</span>
            <div className="absolute inset-0 bg-white opacity-20 transition duration-500 hover:opacity-0"></div>
        </button>
    );
    const RotateLoadingButton = ({ text }: { text: string }) => (
        <button className="bg-gray-700 text-white font-bold py-2 px-4 rounded flex items-center space-x-2">
            <svg className="w-6 h-6 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 4v2a8 8 0 018-8h2"></path></svg>
            <span>{text}</span>
        </button>
    );
    const FadeShareButton = ({ text }: { text: string }) => (
        <button className="bg-indigo-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 transition-opacity duration-300 hover:opacity-75">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M16 5l-4-4-4 4m0 14l4 4 4-4M12 3v18"></path></svg>
            <span>{text}</span>
        </button>
    );
    const ExpandAddButton = ({ text }: { text: string }) => (
        <button className="bg-teal-500 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 transition-transform duration-300 hover:scale-105">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4"></path></svg>
            <span>{text}</span>
        </button>
    );
    const ShakeAlertButton = ({ text }: { text: string }) => (
        <button className="bg-red-600 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 transition-transform duration-75 hover:animate-shake">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10.29 3.86l-8.57 8.57a1 1 0 000 1.41l8.57 8.57a1 1 0 001.41 0l8.57-8.57a1 1 0 000-1.41l-8.57-8.57a1 1 0 00-1.41 0z"></path></svg>
            <span>{text}</span>
        </button>
    );
    const MoveInfoButton = ({ text }: { text: string }) => (
        <button className="bg-blue-700 text-white font-bold py-2 px-4 rounded flex items-center space-x-2 transition-transform duration-300 hover:translate-x-2">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
            <span>{text}</span>
        </button>
    );
    return (
        <>
            <div className='flex flex-wrap w-auto m-32 justify-center content-center gap-20'>
                <InnerShadowButton text={"Entrar"} />
                <PrimaryButton text={"Entrar"} />
                <SecondaryButton text={"Entrar"} />
                <OutlineButton text={"Entrar"} />
                <ShadowButton text={"Entrar"} />
                <GradientButton text={"Entrar"} />
                <AnimatedButton text={"Entrar"} />
                <IconButton text={"Entrar"} />
                <GlassButton text={"Entrar"} />
                <LeftIconAnimatedButton text={"Entrar"} />
                <RightIconSlideButton text={"Entrar"} />
                <PulseIconButton text={"Entrar"} />
                <LoadingIconButton text={"Entrar"} />
                <BorderRotateIconButton text={"Entrar"} />
                <ShineIconButton text={"Entrar"} />
                <PressIconButton text={"Entrar"} />
                <FloatingIconButton text={"Entrar"} />
                <ColorChangeIconButton text={"Entrar"} />
                <GrowIconButton text={"Entrar"} />
                <PulseCheckButton text={"Entrar"} />
                <BounceDownloadButton text={"Entrar"} />
                <HeartBeatButton text={"Entrar"} />
                <ShineStarButton text={"Entrar"} />
                <RotateLoadingButton text={"Entrar"} />
                <FadeShareButton text={"Entrar"} />
                <ExpandAddButton text={"Entrar"} />
                <ShakeAlertButton text={"Entrar"} />
                <MoveInfoButton text={"Entrar"} />
            </div>
        </>
    )
}