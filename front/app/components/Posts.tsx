'use client'

import Image from "next/image";
import Link from "next/link";
import { RichText } from '../utils/rich-text';
import type { RichTextContent } from '@graphcms/rich-text-types'
import Post from "./Post";


export function Posts({ projetos }: any) {
    return (
        <>
            <section className="w-full">
                <div className="py-28 text-center">
                    <h1 className="text-5xl font-semibold text-theme-secondary">Projetos</h1>
                </div>
                <div className="mx-auto grid max-w-7xl grid-cols-1 gap-6 p-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3"> {/* lg:grid-cols-4 */}
                    {projetos && Array.isArray(projetos) && projetos.length > 0 ? (
                        projetos.reverse().map((produto, index) => <Post key={index} produto={produto} />)
                    ) : (
                        <p>Nenhum produto disponível.</p>
                    )}
                </div>
            </section>
        </>
    );
}