'use client'
import { UsuarioService } from "@/data/UsuarioService"
import { Usuario } from "@/domain/Usuario";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import InformacoesUsuario from "./InformacoesUsuario";
import ListarPets from "./ListarPets";

export default function PerfilUsuario() {
  const service = new UsuarioService();
  const router = useRouter();
  const [usuario, setUsuario] = useState<Usuario>();
  useEffect(() => {
    service.obter()
      .then(res => setUsuario(res.data))
      .catch(error => {
        if (error.response.status == 401) {
          router.push("/login");
        }
      });
  }, []);

  return (
    <>
      <div className='antialiased bg-[#0b132d] text-white bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col py-16'>
        <div className="">
          {
            usuario != null ? (
              <InformacoesUsuario usuarioIn={usuario} />
            )
              :
              (
                <h1>teste</h1>
              )
          }
          {
            usuario != null ? (
              <ListarPets petsIn={[]} />
            )
              :
              (
                <h1>teste</h1>
              )
          }
        </div>
      </div>
    </>
  )
}