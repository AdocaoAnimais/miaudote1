'use client'

import Image from "next/image";
import Link from "next/link";

export default function Post({ animal }: any) {
    // console.log(animal.picture.length)
    // console.log(Object.keys(animal.picture).length);

    return (
        <article className="text-white">
            {/* && animal.picture.length > 0  */}
            {/* {animal.picture && (
                <ImageCarousel images={animal.picture} />
            )} */}

            {/* {console.log(animal.picture.length)} */}


            <div className={`relative h-[240px] w-full overflow-hidden rounded-xl`}>
                <Image
                    key={"s"}
                    src={animal.picture.url}
                    alt={`Imagem`}
                    fill
                    style={{ objectFit: 'cover' }}
                />
            </div>

            <Link href={`post/${animal.slug}`}>
                <div className="mt-1 p-2">
                    <h2 className="text-theme-secondary text-lg">{animal.nome}</h2>
                    <div className="mt-1 text-sm text-slate-400 text-justify hyphens-auto">
                        <p>{animal.description}</p>
                    </div>
                    <div className="mt-3 flex items-end justify-between">
                        <p className="text-sm font-semibold text-blue-500">{animal.porte}</p>
                    </div>
                </div>
            </Link>

        </article>
    )
}