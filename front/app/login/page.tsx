'use client'
import LoginForm from "./LoginForm"

export default function Login() {

    return (
        <>
            <div className='antialiased bg-[#0b132d] text-white bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col py-16'>
                <div className="flex flex-wrap gap-32">

                    <LoginForm />

                    <div className="">
                        <div className="img w-[300px] h-[300px]">

                        </div>

                    </div>
                </div>
            </div>
        </>
    )
}