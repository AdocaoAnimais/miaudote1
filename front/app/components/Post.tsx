'use client'

import { PetPost } from "@/domain/Pet";
import Image from "next/image";
import Link from "next/link";
import { useState } from "react";

export default function Post({ animal }: { animal: PetPost }) {
  const [pet, setPet] = useState<PetPost>(animal);

  return (
    <article className="text-white">
      {/* && animal.picture.length > 0  */}
      {/* {animal.picture && (
                <ImageCarousel images={animal.picture} />
            )} */}

      {/* {console.log(animal.picture.length)} */}


      <div className={`relative h-[240px] w-full overflow-hidden rounded-xl`}>
        {pet?.foto != null ?
          <Image
            key={"s"}
            src={pet?.foto}
            alt={`Imagem`}
            fill
            style={{ objectFit: 'cover' }}
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
      <Link href={`perfil-usuario`}>
        <div className="mt-1 p-2">
          <h2 className="text-theme-secondary text-lg">{pet.nome}</h2>
          <div className="mt-1 text-sm text-slate-400 text-justify hyphens-auto">
            <p>{pet.descricao}</p>
          </div>
          <div className="mt-3 flex items-end justify-between">
            <p className="text-sm font-semibold text-blue-500">Porte: {pet?.porte?.nome}</p>
            <p className="text-sm font-semibold text-blue-500">{pet?.tipo?.nome}</p>
            <p className="text-sm font-semibold text-blue-500">Sexo: {pet?.sexo?.nome}</p>
          </div>
        </div>
      </Link>
    </article>
  )
}