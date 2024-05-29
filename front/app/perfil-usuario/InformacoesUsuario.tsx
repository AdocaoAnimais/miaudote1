'use client'

import { Usuario } from "@/domain/Usuario";
import { Ref, useRef, useState } from "react";

export default function InformacoesUsuario({ usuarioIn }) {
  const [ usuario, setUsuario ] = useState<Usuario>(usuarioIn)
  return (
    <>
      <div>
        {usuario.nome + " " + usuario.sobrenome}
      </div>
    </>
  )
}