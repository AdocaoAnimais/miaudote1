'use client'

import { PetPost } from "@/domain/Pet";
import Post from "./Post";

export function Posts({ animais, title, noData, inTelaUsuario }: { animais: PetPost[], title: string, noData: string, inTelaUsuario: boolean }) {
  return (
    <>
      {/* {console.log(animais.length)} */}
      <section className="w-full">
        <div className="py-28 text-center">
          <h1 className="text-2xl font-semibold text-theme-secondary">{title}</h1>
        </div>
        <div className="mx-auto grid max-w-7xl grid-cols-1 gap-6 p-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3">
          {
            Array.isArray(animais) && animais.length > 0
              ? (
                animais.map((animal: PetPost) => <Post key={animal.id} animal={animal} inTelaUsuario={inTelaUsuario}/>)
              ) : (
                <p>{noData}</p>
              )}
        </div>
      </section>
    </>
  );
}