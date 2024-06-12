'use client'
import { UsuarioService } from "@/data/UsuarioService"
import { Usuario } from "@/domain/Usuario";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import InformacoesUsuario from "./InformacoesUsuario";
import { PetService } from "@/data/PetService";
import { PetPost } from "@/domain/Pet";
import { Posts } from "../components/Posts"; 
import { AuthenticationService } from "@/data/AuthenticationService";

export default function PerfilUsuario() {
  const service = new UsuarioService();
  const authService = new AuthenticationService();
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
      }).catch(error => {
        if (error.response.status == 401) {
          router.push("/login");
        }
      });
  }, []);

  async function deletar() {
    await service.deletar().then(() => {
      authService.logout()
      router.push("/cadastrar_animal")
    })
  }

  return (
    <>
      <div className='antialiased bg-[#0b132d] text-white bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col py-16'>
        <div>
          {
            usuario != null && (
              <InformacoesUsuario usuarioIn={usuario}  deletar={deletar} />
            )
          }
          {
            pets?.length != null && (
              <Posts animais={pets}
              inPerfil={true} inAdotados={false}/>
            )
          }
        </div>
      </div>
    </>
  )
}