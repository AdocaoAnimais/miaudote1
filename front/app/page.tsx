'use client'
import React, { use, useState } from 'react';
import Card from './components/Card.tsx';
import Link from 'next/link';
import { AuthenticationService } from '@/data/AuthenticationService';
import Button_Sair from './components/Buttons/Button_Sair';

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
  }

  return (
    <>
      <div className='antialiased bg-slate-600 bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col'>
        <div className='w-full text-right'>
          {
            logado ?
              (
                <div className='w-full p-4 text-right'>
                  <Link href={"/usuario"} className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                    Perfil Usuario
                  </Link>
                </div>
              )

              :
              (
                <div className='w-full p-4 text-right'>
                  <Link href={"/cadastro"} className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                    Cadastrar Usuário
                  </Link>
                </div>
              )
          }
          {
            logado ?
              (
                <div className='p-4 text-right'>
                  <Button_Sair/>
                </div>
              )
              :
              (
                <div className='w-full p-4 text-right'>
                  <Link href={"/login"} className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                    Fazer Login
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
