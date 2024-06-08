'use client'
import Image from "next/image"

import * as React from "react";
import { useState } from "react";
import { DragCat } from "./Dragcat";

export default function Header() {
    const [count, setCount] = useState(0);

    return (
        <>
            <div className="p-12 w-full justify-center flex flex-wrap gap-9 bg-gray-900">

                <div className="m-6 relative w-[280px] h-[280px] overflow-hidden rounded-xl justify-center items-center content-center">
                    <Image
                        src={"/logo.png"}
                        alt="Minha imagem"
                        fill
                        style={{ objectFit: 'contain' }} // cover
                    />
                </div>

                <div className="text-theme-text2 max-w-[700px] p-12 overflow-hidden justify-center items-center content-center">
                    <h1 className="font-bold text-4xl py-7">Sobre</h1>
                    <p>Bem-vindo à nossa plataforma de adoção de animais, um refúgio digital onde histórias de amor entre humanos e animais ganham vida. Somos apaixonados por conectar animais necessitados a lares amorosos, transformando vidas de ambos os lados da tela.</p>
                </div>
            </div>
            <div className="w-5/6">
                <DragCat />
            </div>
        </>
    )
}