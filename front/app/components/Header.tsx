'use client'
import Image from "next/image"

import * as React from "react";
import { useEffect, useState } from "react";
import { DragCat } from "./Dragcat";
import { motion, Variants } from "framer-motion";

export default function Header() {

    const cardVariants: Variants = {
        offscreen_right: {
            x: 150,
            rotate: -2,
        },
        offscreen_left: {
            x: -150,
            rotate: 2
        },
        offscreen_down: {
            y: 90,
            rotate: -2
        },
        onscreen: {
            y: 0,
            x: 0,
            rotate: 0,
            transition: {
                type: "spring",
                bounce: 0.2,
                duration: 1.2
            }
        }
    };

    return (
        <>
            <div className="p-12 w-full justify-center flex flex-wrap gap-9 bg-gray-900">
                <motion.div
                    variants={cardVariants}
                    initial="offscreen_left"
                    whileInView="onscreen"
                    viewport={{ once: false, amount: 0.1 }}
                >
                    <div className="m-6 relative w-[280px] h-[280px] overflow-hidden rounded-xl justify-center items-center content-center">
                        <Image
                            src={"/logo.png"}
                            alt="Minha imagem"
                            fill
                            style={{ objectFit: 'contain' }} // cover
                        />
                    </div>
                </motion.div>
                <motion.div
                    variants={cardVariants}
                    initial="offscreen_right"
                    whileInView="onscreen"
                    viewport={{ once: false, amount: 0.1 }}
                >
                    <div className="text-theme-text2 max-w-[700px] p-12 overflow-hidden justify-center items-center content-center">
                        <h1 className="font-bold text-4xl py-7">Sobre</h1>
                        <p>Bem-vindo à nossa plataforma de adoção de animais, um refúgio digital onde histórias de amor entre humanos e animais ganham vida. Somos apaixonados por conectar animais necessitados a lares amorosos, transformando vidas de ambos os lados da tela.</p>
                    </div>
                </motion.div>
            </div>
            {/* <div className="w-5/6">
                <DragCat />
            </div> */}
        </>
    )
}