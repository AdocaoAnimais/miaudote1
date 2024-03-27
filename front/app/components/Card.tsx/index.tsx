'use client'

import { Posts } from "../Posts"


export default function Card() {

    const posts1 = [
        {
          slug: "post-1",
          title: "Título do Post 1",
          rich: {
            raw: "Conteúdo do Post 1 em formato raw"
          },
          picture: {
            url: "https://github.com/weltergab/png"
          },
          videoUrl: "URL do vídeo do Post 1",
          tech: "Tecnologia relacionada ao Post 1",
          description: "Descrição do Post 1"
        },
        {
          slug: "post-2",
          title: "Título do Post 2",
          rich: {
            raw: "Conteúdo do Post 2 em formato raw"
          },
          picture: {
            url: "https://github.com/weltergab/png"
          },
          videoUrl: "URL do vídeo do Post 2",
          tech: "Tecnologia relacionada ao Post 2",
          description: "Descrição do Post 2"
        },
        // Adicione mais objetos conforme necessário para mais posts
      ];
      
      console.log(posts1);

    return (
        <>
        <Posts projetos={posts1} />






        <br />
            <div className="blue h-[300px] w-[300px]">
                <h1>Nome do Animal</h1>
                <div className="blue h-[300px] w-[300px]">
                    <p>Imagem do Animal</p>
                </div>
                <div>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Excepturi illum, ullam architecto expedita aliquid sequi iste nemo delectus nulla adipisci tempora eius odit eligendi exercitationem commodi minus illo sint. Eos.</p>
                </div>
            </div>
            
            <div>
                <button className="">

                </button>
            </div>
        </>
    )
}
