'use client'

import { Ref, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import Image from "next/image";
import backIcon from "../../public/back.svg";
import Link from "next/link";
import { AuthenticationService } from "@/data/AuthenticationService";
import Alert from '@mui/material/Alert';
import Stack from '@mui/material/Stack';
import { AxiosError } from "axios";

export default function LoginForm({ }) {
    const service = new AuthenticationService();

    const router = useRouter()
    const ref: Ref<any> = useRef(null);

    const [error, setError] = useState<string | null>(null);

    init();
    async function init() {
        try {
            const response = await service.logged();
            if (response) {
                console.log("Usuário já logado, redirecionando para tela inicial.");
                router.push("/");
            }
        } catch (e) {
            console.log(e)
        }
    }
    const handleSubmit = async (event: any) => {
        event.preventDefault();
        setError(null); // Reset error state before attempting login

        const username = ref.current.username.value;
        const senha = ref.current.senha.value;

        try {
            await service.loggin(username, senha)
            console.log("Logado com sucesso!");
            router.refresh();
        } catch (e) {
            console.log("Erro ao efetuar login: ", e.detail);
            setError(e.detail || "Erro desconhecido ao efetuar login");
        }
    };

    return (
        <>
            {/* REsponse > data > title
        data > detail  */}
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
                        className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-500 focus:ring-opacity-20"
                        placeholder="Usuário"
                        required
                    />
                </div>
                <div className="mb-4 md:col-span-2">
                    <input
                        id="senha"
                        name="senha"
                        type="password"
                        className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-500 focus:ring-opacity-80"
                        placeholder="Senha"
                        required
                    />
                </div>
                <div>

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
                        <Link href={"/cadastro"} className=" text-white hover:text-[#f2a812]">
                            Não tem cadastro? Cadastre-se!
                        </Link>
                    </div>
                </div>
                <div className="md:col-span-2">
                    {error && (
                        <Stack sx={{ width: '100%' }} spacing={2}>
                            <Alert severity="error">{error}</Alert>
                        </Stack>
                    )}
                </div>
            </form>
        </>
    )
}