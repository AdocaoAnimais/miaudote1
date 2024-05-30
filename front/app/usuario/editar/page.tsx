'use client'
import { UsuarioService } from "@/data/UsuarioService"
import { Usuario } from "@/domain/Usuario";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";  
import { AuthenticationService } from "@/data/AuthenticationService";

export default function PerfilUsuario() {
  const service = new UsuarioService(); 
  const authService = new AuthenticationService()
  const router = useRouter();
  const [usuario, setUsuario] = useState<Usuario>();

  init();
  async function init() {
    const response = await authService.logged(); 
    if(!response){
      router.push("/")
    }
  }
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
        <div>
          <p>editar infos usuario</p>
        </div>
      </div>
    </>
  )
}