'use client'

import { Usuario } from "@/domain/Usuario";
import { useState } from "react";
import Button_Sair from "../components/Buttons/Button_Sair";
import Link from "next/link";
import ToastDemo from "../components/Mensagem";
import { UsuarioService } from "@/data/UsuarioService";

export default function InformacoesUsuario({ usuarioIn, deletar }: { usuarioIn: Usuario, deletar: Function }) {
  const service = new UsuarioService()
  const [usuario] = useState<Usuario>(usuarioIn)
  const [ error, setError ] = useState<{ title: string, detalhes: string } | null>(null)
  function fecharModal(){
    if(error?.title == "Validar email"){
      setError(null)
    } else {
      deletar()
    }
  }

  async function validarEmail(){
    await service.enviarEmailValidacao();
    setError({
      title: "Validar email",
      detalhes: "Enviamos um email para realizar a validação!"
    })
  } 

  function confirmarDeletar() {
    setError({
      title: "Apagar Conta",
      detalhes: "Tem certeza que deseja apagar a sua conta? "
    })
  }
  return (
    <>
      <div className="min-w-full border border-gray-400 p-20">
        <div className="leading-normal grid grid-cols-2">
          <div>
            <div className="m-5">
              <div className="text-white-900 font-bold text-xl mb-2">{usuario.nome} {usuario.sobrenome}</div>
              <p className="text-white-800 font-bold text-lg">@{usuario.username}</p>
              <p className="text-white-700 font-bold text-md">{usuario.email}</p>
              <p className="text-white-700 text-sm">{usuario.descricao}</p>
              <p className="text-white-700 text-base">CPF: {usuario.cpf}</p>
              <p className="text-white-700 text-base">Contato: {usuario.contato}</p>
            </div>
            <div>
              <Link href={"/usuario/editar"} className="m-2 py-5 px-10 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                Editar
              </Link> 
              <button onClick={validarEmail} className="m-2 py-4 px-10 bg-yellow-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                Validar email
              </button> 
              <Button_Sair />
              <button onClick={confirmarDeletar}  className="m-2 py-4 px-10 bg-red-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                Apagar conta
              </button> 
            </div>
          </div>
          <div className="justify-self-end">
            <img
              className="h-48 lg:h-auto lg:w-48 "
              src="/logo.png"
            />
          </div>
          {error && <ToastDemo title={error.title} detalhes={error.detalhes} fecharModal={fecharModal} />}
        </div>
      </div>
    </>
  )
}