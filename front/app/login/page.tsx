'use client'
import Image from "next/image"
import LoginForm from "./LoginForm"

export default function Login() {

    return (
        <>
            <div className='antialiased bg-theme-bg text-theme-text bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col py-16'>
                <div className="flex flex-wrap gap-32">

                    <div>
                        <LoginForm />
                    </div>
                    <div className="justify-center items-center content-center">
                        <div className="relative w-[420px] h-[420px] overflow-hidden rounded-xl">
                            <Image
                                src={"/logo.png"}
                                alt="Minha imagem"
                                fill
                                style={{ objectFit: 'contain' }} // cover
                            />
                            {/* <img src={produto.image} alt="Coffee" /> */}
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}