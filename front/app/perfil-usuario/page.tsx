'use client'
import { UsuarioService } from "@/data/UsuarioService"
import { Usuario } from "@/domain/Usuario";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import InformacoesUsuario from "./InformacoesUsuario";
import { PetService } from "@/data/PetService";
import { PetPost } from "@/domain/Pet";
import { Posts } from "../components/Posts";

export default function PerfilUsuario() {
  const service = new UsuarioService();
  const petService = new PetService();
  const router = useRouter();
  const [usuario, setUsuario] = useState<Usuario>();

  const [pets, setPets] = useState<PetPost[]>();

  useEffect(() => {
    service.obter()
      .then(res => setUsuario(res.data))
      .catch(error => {
        if (error.response.status == 401) {
          router.push("/login");
        }
      });

    petService.obterPetsUsuario()
      .then(res => {
        setPets(res.data)
      })
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
                <h2>Usuário não econtrado</h2>
              )
          }
          {
            pets?.length != null ? (
              <Posts animais={pets}
                title="Seus animais cadastrados"
                noData="Nenhum animal cadastrado até o momento."
                inTelaUsuario={true}
              />
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