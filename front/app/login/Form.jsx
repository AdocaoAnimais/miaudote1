'use client'

import { useEffect, useRef, useState } from "react";
import { loginUser } from "../services/PetService";
import { toast, ToastContainer } from "react-toastify";
import { useRouter } from "next/navigation";

export default function Form({ }) {
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
                className="max-w-lg mx-auto grid grid-cols-1 md:grid-cols-2 gap-4"
            > 
                <div className="mb-4 md:col-span-2">
                    <label htmlFor="username" className="block text-sm font-medium text-gray-700">
                        Username:
                    </label>
                    <input
                        id="username"
                        name="username"
                        type="text"
                        className="mt-1 p-3 block w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        placeholder="Digite o seu username"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label htmlFor="senha" className="block text-sm font-medium text-gray-700">
                        Senha:
                    </label>
                    <input
                        id="senha"
                        name="senha"
                        type="password"
                        className="mt-1 p-3 block w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        placeholder="Digite sua senha"
                        required
                    />
                </div>

                <div className="flex justify-end md:col-span-2">
                    <button
                        type="button"
                        href={"/cadastro-usuario"}
                        className="py-2 px-4 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
                    >
                        Não tenho Login
                    </button>
                    <button
                        type="submit"
                        className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:bg-indigo-700"
                    >
                        Login
                    </button>
                </div>
            </form>
        </>
    )
}
