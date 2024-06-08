'use client'
import Form from "./Form";
import { Usuario } from "@/domain/Usuario";
import { useEffect, useState } from "react";
import { UsuarioService } from "@/data/UsuarioService"; 
import { useRouter } from "next/navigation";

export default function Page() {
  const router = useRouter();
  const service = new UsuarioService();
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
      <div className='antialiased bg-theme-bg text-theme-text bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col py-16'>
        <div className="flex flex-wrap gap-32">

          <div className="text-theme-text2">
            {
              usuario && (
                <Form usuario={usuario} />
              )
            }
          </div>
        </div>
      </div>
    </>
  )
}