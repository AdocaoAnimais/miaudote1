'use client'

import Link from "next/link";
import { Posts } from "../Posts"

export default function Card() {

  const animais = [
    {
      slug: "post-1",
      nome: "Nome do animal",
      picture: {
        url: "https://github.com/AdocaoAnimais.png"
      },
      porte: "Porte Médio",
      description: "Lorem, ipsum dolor sit amet consectetur adipisicing elit. Libero, ipsum. Laboriosam est aperiam architecto a..."
    },
    {
      slug: "post-1",
      nome: "Nome do animal",
      picture: {
        url: "https://github.com/AdocaoAnimais.png"
      },
      porte: "Porte Médio",
      description: "Lorem, ipsum dolor sit amet consectetur adipisicing elit. Libero, ipsum. Laboriosam est aperiam architecto a..."
    },
    {
      slug: "post-1",
      nome: "Nome do animal",
      picture: {
        url: "https://github.com/AdocaoAnimais.png"
      },
      porte: "Porte Médio",
      description: "Lorem, ipsum dolor sit amet consectetur adipisicing elit. Libero, ipsum. Laboriosam est aperiam architecto a..."
    },
    {
      slug: "post-1",
      nome: "Nome do animal",
      picture: {
        url: "https://github.com/AdocaoAnimais.png"
      },
      porte: "Porte Médio",
      description: "Lorem, ipsum dolor sit amet consectetur adipisicing elit. Libero, ipsum. Laboriosam est aperiam architecto a..."
    },
    {
      slug: "post-1",
      nome: "Nome do animal",
      picture: {
        url: "https://github.com/AdocaoAnimais.png"
      },
      porte: "Porte Médio",
      description: "Lorem, ipsum dolor sit amet consectetur adipisicing elit. Libero, ipsum. Laboriosam est aperiam architecto a..."
    },
    // Adicione mais objetos conforme necessário
  ];

  // console.log(animais);

  return (
    <>
      <Posts animais={animais} />
    </>
  )
}
