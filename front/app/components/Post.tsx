'use client'

import Image from "next/image";
import Link from "next/link";
import { PetPost } from "../services/PetService";
import { useState } from "react";

export default function Post({ animal }: { animal: PetPost }) {
    const [pet, setPet] = useState<PetPost>(animal)
    const truncateDescription = (text: string, maxLength: number) => {
        if (text.length <= maxLength) return text;
        return text.slice(0, maxLength) + '...';
    };

    return (
        <Link href={`post/${pet.id}`}>
            <div className="h-[440px] rounded-lg overflow-hidden shadow-lg bg-gray-800 transform transition-transform hover:scale-105">
                <div className="relative w-auto h-[240px] overflow-hidden rounded-t-lg">
                    {/* <Image src={"/logo.png"} alt={"title"} width={400} height={400} className="rounded-lg" /> */}
                    {pet?.foto != null ?
                        <Image
                            key={"s"}
                            src={pet?.foto}
                            alt={`Imagem`}
                            fill
                            style={{ objectFit: 'cover' }} // contain
                        />
                        :
                        <Image
                            key={"s"}
                            src={"https://igp.rs.gov.br/themes/modelo-noticias/images/outros/GD_imgSemImagem.png"}
                            alt={`Imagem`}
                            fill
                            style={{ objectFit: 'cover' }}
                        />
                    }
                </div>
                <div className="p-8 text-white">
                    <div className="font-bold text-2xl mb-2 text-theme-primary">{pet.nome}</div>
                    <div className="flex space-x-2 mb-2">
                        <span className="bg-blue-500 text-white text-xs font-semibold px-2.5 py-0.5 rounded-full">{pet?.porte?.nome}</span>
                        <span className="bg-pink-500 text-white text-xs font-semibold px-2.5 py-0.5 rounded-full">{pet?.porte?.nome}</span>
                    </div>
                    <p className="text-gray-300 text-base">
                        {truncateDescription(pet.descricao, 100)}
                    </p>
                </div>
            </div>
        </Link>
    );
};
