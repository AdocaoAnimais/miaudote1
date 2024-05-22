'use client'

import { PetPost } from "../services/PetService";
import Post from "./Post";

export function Posts({animais} : { animais: PetPost[] }) {
    return (
        <>
        {/* {console.log(animais.length)} */}
            <section className="w-full">
                <div className="py-28 text-center">
                    <h1 className="text-5xl font-semibold text-theme-secondary">Animais</h1>
                </div>
                <div className="mx-auto grid max-w-7xl grid-cols-1 gap-6 p-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3"> 
                    {
                    Array.isArray(animais) && animais.length > 0 
                    ? (
                        animais.map((animal: PetPost) => <Post key={animal.id} animal={animal} />)
                    ) : (
                        <p>Nenhum animal disponível.</p>
                    )}
                </div>
            </section>
        </>
    );
}