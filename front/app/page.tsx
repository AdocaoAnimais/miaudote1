'use client'
import React, { use, useState } from 'react';
import Card from './components/Card.tsx';
import Link from 'next/link';
import { AuthenticationService } from '@/data/AuthenticationService';
import LinkButton_YellowTarja from './components/Buttons/LinkButton_YellowTarja.tsx';

export default function Page() {
    const service = new AuthenticationService();
    init();

    const [logado, setLogado] = useState(false);

    async function init() {
        const response = await service.logged();
        setLogado(response)
    }

    function loggout() {
        service.logout();
        setLogado(false)
    }

    return (
        <>
            <div className='antialiased bg-theme-bg bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col'>
                <div className='w-full text-right'>
                    {
                        logado ?
                            (
                                <div className='w-full p-4 text-right' >
                                    <Link href={"/cadastrar_animal"}>
                                        <LinkButton_YellowTarja texto="Cadastrar novo animal" />
                                    </Link>
                                </div>
                            )
                            :
                            (
                                <div className='w-full p-4 text-right'>
                                    <Link href={"/cadastro"}>
                                        <LinkButton_YellowTarja texto="Cadastrar Usuário" />
                                    </Link>
                                </div>
                            )
                    }
                    {
                        logado ?
                            (
                                <div className='p-4 text-right'>
                                    <button
                                        onClick={loggout}
                                        type="button"
                                    >
                                        <LinkButton_YellowTarja texto="Sair" />
                                    </button>
                                </div>

                            )
                            :
                            (
                                <div className='w-full p-4 text-right'>
                                    <Link href={"/login"} >
                                        <LinkButton_YellowTarja texto="Fazer Login" />
                                    </Link>
                                </div>
                            )
                    }
                </div>
                <Card />
            </div>
        </>
    )
}
