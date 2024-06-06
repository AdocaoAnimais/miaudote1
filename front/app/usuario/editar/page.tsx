'use client'
import Link from "next/link";
import Image from 'next/image';
import backIcon from "../../../public/back.svg";
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


          <div className="">
            <div className="justify-center items-center content-center text-center">
              <div className="relative w-[200px] h-[200px] overflow-hidden rounded-xl">
                <Image
                  src={"/logo.png"}
                  alt="Minha imagem"
                  fill
                  style={{ objectFit: 'contain' }} // cover
                />
                {/* <img src={produto.image} alt="Coffee" /> */}
              </div>
            </div>
            <div>
              <h1 className="py-4">Editar perfil</h1>
              <p className="text-[#737380]">Edite suas informações!</p>
            </div>
            <div className='w-full gap-2 flex p-12 text-left pt-32 pb-2 px-6'>
              <Image
                priority
                src={backIcon}
                alt="Follow us on Twitter"
              />
              <Link href={"/usuario"} className=" text-theme-text hover:text-theme-texthighlight">
                Voltar para perfil
              </Link>
            </div>
          </div>
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