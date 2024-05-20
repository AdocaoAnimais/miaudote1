'use client'

import { useEffect, useRef, useState } from "react";
import { loginUser } from "../services/PetService";
import { toast, ToastContainer } from "react-toastify";
import { useRouter } from "next/navigation";
import Image from "next/image";
import backIcon from "../../public/back.svg";
import Link from "next/link";

export default function LoginForm({ }) {
    const router = useRouter()
    const ref = useRef(null);
    const handleSubmit = (event) => {
        event.preventDefault();

        const username = ref.current.username.value;
        const senha = ref.current.senha.value;

        try {
            loginUser(username, senha);
            console.log("Logado com sucesso!");
            router.push("/cadastrar_animal");
        } catch (e) {
            console.log(e)
        }
    };

    return (
        <>
            <form
                ref={ref}
                onSubmit={handleSubmit}
                className="max-w-lg mx-auto grid grid-cols-1 md:grid-cols-2 gap-2 mt-72"
            >
                <h1 className="font-bold">Faça seu login</h1>
                <div className="md:col-span-2">
                    <input
                        id="username"
                        name="username"
                        type="text"
                        className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        placeholder="Usuário"
                        required
                    />
                </div>
                <div className="mb-4 md:col-span-2">
                    <input
                        id="senha"
                        name="senha"
                        type="password"
                        className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        placeholder="Senha"
                        required
                    />
                </div>

                <div className="justify-start md:col-span-2">
                    <button
                        type="submit"
                        className="h-[50px] w-full px-20 bg-[#f2a812] text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:bg-indigo-700"
                    >
                        Entrar
                    </button>
                    <div className='w-full gap-2 flex p-12 text-left pb-2 px-6'>
                        <Image
                            priority
                            src={backIcon}
                            alt="Cadastro"
                        />
                        <Link href={"/cadastro-usuario"} className=" text-white hover:text-[#f2a812]">
                            Não tem cadastro? Cadastre-se!
                        </Link>
                    </div>
                </div>
            </form>
        </>
    )
}