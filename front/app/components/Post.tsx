'use client'

import { PetService } from "@/data/PetService";
import { PetPost } from "@/domain/Pet";
import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import ErrorMessage from "./ErrorMesagem";
import ToastDemo from "./Mensagem";

export default function Post({ animal, inPerfil, isLogado }: { animal: PetPost, inPerfil: boolean, isLogado: boolean }) {
    const router = useRouter()
    const [error, setError] = useState<{ title: string; detalhes: string } | null>(null);
    const service = new PetService();
    const [pet, setPet] = useState<PetPost>(animal)
    const [imageSrc, setImageSrc] = useState(pet.imageData ? 'data:image/jpeg;base64,' + pet.imageData : null)
    const [deletado, setDeletado] = useState(false)
    const truncateDescription = (text: string, maxLength: number) => {
        if (text.length <= maxLength) return text;
        return text.slice(0, maxLength) + '...';
    };

    async function solicitarAdocao(event: any) {
        if (!isLogado) {
            router.push("/login")
        } else {
            const id = event.target.id
            await service.solicitarAdocao(id).then(res => {
                alert(res.response)
            }).catch((res: any) => {
                const title = res.response.data.title
                const detalhes = res.response.data.detail
                // alert(`${title} ${detalhes}`)
                setError({ title, detalhes });
            });
        }
    }
    async function deletar(event: any) {
        console.log(event.target.id)
        const id = event.target.id
        await service.deletar(id).then(res => {
            setDeletado(true)
        }).catch((res: any) => {
            console.log(res)
        });
        router.refresh()
    }

    return (
        deletado ? <></> :
            <div className="h-[440px] rounded-lg overflow-hidden shadow-lg bg-gray-800 transform transition-transform hover:scale-105">
                <div className="relative w-auto h-[240px] overflow-hidden rounded-t-lg">
                    {/* <Image src={"/logo.png"} alt={"title"} width={400} height={400} className="rounded-lg" /> */}
                    {imageSrc ?
                        <Image
                            key={"s"}
                            src={imageSrc}
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
                <Link href={`post/${pet.id}`}>
                    <div className="pt-2 pb-0 pl-4 text-white">
                        <div className="font-bold text-2xl mb-2 text-theme-primary">{pet.nome}</div>
                        <div className="flex space-x-2 mb-2">
                            <span className="bg-blue-500 text-white text-xs font-semibold px-2.5 py-0.5 rounded-full">{pet?.porte.nome}</span>
                            <span className="bg-pink-500 text-white text-xs font-semibold px-2.5 py-0.5 rounded-full">{pet?.castrado.nome}</span>
                            <span className="bg-green-500 text-white text-xs font-semibold px-2.5 py-0.5 rounded-full">{pet?.sexo.nome}</span>
                        </div>
                        <p className="text-gray-300 text-base">
                            {truncateDescription(pet.descricao, 100)}

                        </p>
                    </div>
                </Link>
                {
                    inPerfil ? (
                        <>
                            <Link
                                href={`/editar_animal/${pet.id}`}
                            >
                                <button
                                    id={`${pet.id}`}
                                    className="py-2 px-6 m-2 bg-theme-button1 text-theme-text rounded hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
                                >
                                    Editar
                                </button>
                            </Link>
                            <button
                                onClick={deletar}
                                id={`${pet.id}`}
                                className="py-2 px-6 m-2 bg-theme-button2 text-theme-text rounded hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
                            >
                                Deletar
                            </button>
                        </>

                    ) :
                        (
                            <div>
                                <button
                                    id={`${pet.id}`}
                                    className="py-2 px-6 m-2 bg-theme-button1 text-theme-text rounded hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
                                    onClick={solicitarAdocao}
                                >
                                    Adotar
                                </button>
                                {error && <ToastDemo title={error.title} detalhes={error.detalhes} />}
                            </div>
                        )
                }
            </div>
    );
};
