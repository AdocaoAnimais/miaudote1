'use client'

import Post from "./Post";

export function Posts({ animais }: any) {
    return (
        <>
        {/* {console.log(animais.length)} */}
            <section className="w-full">
                <div className="py-28 text-center">
                    <h1 className="text-5xl font-semibold text-theme-secondary">Animais</h1>
                </div>
                <div className="mx-auto grid max-w-7xl grid-cols-1 gap-6 p-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3"> {/* lg:grid-cols-4 */}
                    {animais && Array.isArray(animais) && animais.length > 0 ? (
                        animais.reverse().map((animal, index) => <Post key={index} animal={animal} />)
                    ) : (
                        <p>Nenhum animal disponível.</p>
                    )}
                </div>
            </section>
        </>
    );
}