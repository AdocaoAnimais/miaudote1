'use client'
import { UsuarioService } from "@/data/UsuarioService"
import { useState } from "react";



export default function PerfilUsuario() {
    const service = new UsuarioService();

    const [usuario, setUsuario] = useState();

    init();
    async function init() {
        await service.obter().then((res) => {
            setUsuario(res.data);
            console.log(usuario)
        });
    };

    return (
        <>
            <div className='antialiased bg-[#0b132d] text-white bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col py-16'>
                <div className="flex flex-wrap gap-32">
                    olá mundos
                </div>
            </div>
        </>
    )
}