'use client'

import Image from "next/image";
import { RichText } from '../utils/rich-text';
import Link from "next/link";
import { ImageCarousel } from "./ImageCarousel";

export default function Post({ produto }: any) {
    // console.log(produto.picture)

    return (
        <article className="text-white">

            {produto.picture && produto.picture.length > 0 && (
                <ImageCarousel images={produto.picture} />
            )}

            <Link href={`post/${produto.slug}`}>
                <div className="mt-1 p-2">
                    <h2 className="text-theme-secondary text-lg">{produto.title}</h2>
                    <div className="mt-1 text-sm text-slate-400 text-justify hyphens-auto">
                        {/* <RichText content={produto.rich.raw} /> */}
                        <p>{produto.description}</p>
                    </div>

                    <div className="mt-3 flex items-end justify-between">
                        <p className="text-sm font-semibold text-blue-500">{produto.tech}</p>
                    </div>
                </div>
            </Link>
            
        </article>
    )
}


// type Product = {
//     image: string;
//     name: string;
//     text: string;
//     price: string;
//     stripeProduct: string;
// };

// type PictureProps = {
//     produto: Product;
// };

{/* {produto.picture.map((imagem: any, index: any) => (
                        imagem.url && (
                            <div className="relative h-[240px] w-full overflow-hidden rounded-xl" key={index}>
                                <Image
                                    key={index}
                                    src={imagem.url}
                                    alt={`Imagem ${index + 1}`}
                                    fill
                                    style={{ objectFit: 'cover' }}
                                />
                            </div>
                        )
                    ))} */}

{/* <div className="flex items-center space-x-1.5 rounded-lg bg-blue-500 px-4 py-1.5 text-white duration-100 hover:bg-blue-600">
                        
                            ICONE - Carrinho de compras
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" className="h-4 w-4">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 00-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 00-16.536-1.84M7.5 14.25L5.106 5.272M6 20.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm12.75 0a.75.75 0 11-1.5 0 .75.75 0 011.5 0z" />
                            </svg> 

                    </div> */}

{/* <Image
                        src={produto.picture.url}
                        alt="Minha imagem"
                        fill
                        style={{ objectFit: 'cover' }}
                    /> */}
{/* {imagens} */ }
{/* {produto.picture.url} */ }
{/* <img src={produto.image} alt="Coffee" /> */ }