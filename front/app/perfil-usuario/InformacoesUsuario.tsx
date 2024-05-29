'use client'

import { Usuario } from "@/domain/Usuario";
import { useState } from "react";

export default function InformacoesUsuario({ usuarioIn } : { usuarioIn: Usuario }) {
  const [ usuario ] = useState<Usuario>(usuarioIn)
  return (
    <> 
      <section className="w-full">
        <div className="mx-auto grid max-w-8xl p-6 ">
          {usuario.nome} {usuario.sobrenome}
        </div>
      </section>
    </>
  )
}