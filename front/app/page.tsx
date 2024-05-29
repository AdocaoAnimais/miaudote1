'use client'
import React, { use, useState } from 'react';
import Card from './components/Card.tsx';
import Link from 'next/link';
import { AuthenticationService } from '@/data/AuthenticationService';

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
      <div className='antialiased bg-slate-600 bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col'>
        <div className='w-full text-right'>
          {
            logado ?
              (
                <div className='w-full p-4 text-right'>
                  <Link href={"/perfil-usuario"} className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
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
                  <button
                    onClick={loggout}
                    type="button"
                    className="py-4 px-12 bg-[#0b132d] border border-[#f2a812] text-white rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
                  >
                    Sair
                  </button>
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
