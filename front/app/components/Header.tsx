'use client'
import Image from "next/image"

export default function Header() {

    return (
        <>
            
                <div className="p-24 w-full justify-center flex flex-wrap gap-9 bg-gray-900">
                   
                        <div className="p-12 relative w-[420px] h-[420px] overflow-hidden rounded-xl justify-center items-center content-center">
                            <Image
                                src={"/logo.png"}
                                alt="Minha imagem"
                                fill
                                style={{ objectFit: 'contain' }} // cover
                            />
                            {/* <img src={produto.image} alt="Coffee" /> */}
                        </div>
                

                    <div className="text-theme-text2 max-w-[700px] p-12 overflow-hidden justify-center items-center content-center">
                        <h1 className="font-bold text-4xl py-7">Sobre</h1>
                        <p>Somos Lorem ipsum dolor sit amet consectetur adipisicing elit. Asperiores est, mollitia quos quia adipisci optio quo? Non, tempore fugiat. Accusamus ab, eaque ullam necessitatibus hic aut non tempora voluptates fuga!</p>
                    </div>
                </div>
            
        </>
    )
}