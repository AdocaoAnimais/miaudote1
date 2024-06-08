'use client'

import Post from "./Post";
import LinkButton_YellowTarja from "./Buttons/LinkButton_YellowTarja";
import { AuthenticationService } from '@/data/AuthenticationService';
import { useState } from "react";
import Link from "next/link";
import { PetPost } from "@/domain/Pet";
import { motion, Variants } from "framer-motion";


export function Posts({ animais, inPerfil, inAdotados }: { animais: PetPost[], inPerfil: boolean, inAdotados: boolean }) {

    const service = new AuthenticationService();
    init();

    const [logado, setLogado] = useState(false);

    async function init() {
        const response = await service.logged();
        setLogado(response)
    }

    const cardVariants: Variants = {
        offscreen_right: {
            x: 150,
            rotate: 2
        },
        offscreen_left: {
            x: -150,
            rotate: -2
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
            {/* {console.log(animais.length)} */}
            <section className="w-full py-12 mx-auto max-w-7xl overflow-hidden">

                <motion.div
                    variants={cardVariants}
                    initial="offscreen_right"
                    whileInView="onscreen"
                    viewport={{ once: false, amount: 0.1 }}
                >
                    {
                        logado ?
                            (
                                <div className='w-full p-4 justify-end items-center text-leth' >
                                    <Link href={"/cadastrar_animal"} className="inline-flex">
                                        <LinkButton_YellowTarja texto="Cadastrar animal" />
                                    </Link>
                                </div>
                            )
                            : // Vai para login !!!
                            (
                                <div className='w-full p-4 justify-end items-center text-right'>
                                    <Link href={"/login"} className="inline-flex">
                                        <LinkButton_YellowTarja texto="Cadastrar animal" />
                                    </Link>
                                </div>
                            )
                    }
                </motion.div>
                <div className="py-28 text-center">
                    <h1 className="text-5xl font-bold text-theme-text">Animais {inAdotados ? "adotados" : "cadastrados"}</h1>
                </div>
                <div className="mx-auto grid max-w-7xl grid-cols-1 gap-9 p-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3">
                    {
                        Array.isArray(animais) && animais.length > 0
                            ? (
                                animais.map((animal: PetPost) => <Post key={animal.id} animal={animal} inPerfil={inPerfil} isLogado={logado} inAdotados={inAdotados} />)
                            ) : inAdotados ? (
                                <p>Nenhum animal adotado até o momento :(</p>
                            ) : (
                                <p>Nenhum animal disponível.</p>
                            )}
                </div>
            </section>
        </>
    );
}