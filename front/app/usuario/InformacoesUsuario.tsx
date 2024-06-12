'use client'

import { Usuario } from "@/domain/Usuario";
import { useState } from "react";
import Button_Sair from "../components/Buttons/Button_Sair";
import Link from "next/link";

export default function InformacoesUsuario({ usuarioIn }: { usuarioIn: Usuario }) {
  const [usuario] = useState<Usuario>(usuarioIn)
  return (
    <>
      <div className="min-w-full border-2 rounded-xl border-theme-border p-20">
        <div className="leading-normal grid grid-cols-2">
          <div>
            <div className="m-5">
              <div className="text-white-900 font-bold text-xl mb-2">{usuario.nome} {usuario.sobrenome}</div>
              <p className="text-white-800 font-bold text-lg">@{usuario.username}</p>
              <p className="text-white-700 font-bold text-md">{usuario.email}</p>
              <p className="text-white-700 text-sm">{usuario.descricao}</p>
              <p className="text-white-700 text-base">CPF: {usuario.cpf}</p>
            </div>
            <div>
              <Link href={"/usuario/editar"} className="m-2 py-5 px-10 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                Editar
              </Link> 
              <Button_Sair />
            </div>
          </div>
          <div className="justify-self-end">
            <img
              className="h-48 lg:h-auto lg:w-48 "
              src="/logo.png"
            />
          </div>
        </div>
      </div>
    </>
  )
}