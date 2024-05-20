'use client'

import axios from "axios";
import { useEffect, useState } from "react";
import api from "../App";

type PetPost = {
    pet_id: number,
    nome: string,
    sexo: string,
    idade: number,
    porte: string,
    castrado: string,
    descricao: string,
    foto: Blob,
}

type PostProps = {
    id: string;
    userId: number;
    title: string;
    body: string;
}

type PetsType = {
    "nome": "pet1",
    "idade": "1",
    "sexo": "F",
    "porte": "P",
    "tipo": "G",
    "castrado": "N",
    "descricao": "",
    "usuario": "5"
}




const getPosts = async () => {
    // const response = await axios.get<PostProps[]>("https://jsonplaceholder.typicode.com/posts");
    // const response = await axios.get<Array<PetPost>>("http://127.0.0.1:8000/api/pets");
    const response = await axios.get<PetsType[]>("http://200.132.38.218:8004/api/pet/");
    console.log(response.data)
    return response.data;
};

const createPost = async (data: {
    // title: string;
    // body: string;
    // userId: number;
    nome: string;
    idade: number;
    sexo: string;
    porte: string;
    tipo: string;
    castrado: string;
    descricao: string;
    usuario: number;
}) => {
    const response = await axios.post(
        'http://127.0.0.1:8080/api/pet/',
        data
    );
    return response.data
}



export default async function Page() {
    const posts = await getPosts();
    const [pets, setPets] = useState(); 

    useEffect(() => {
        api
            .get("http://127.0.0.1:8080/api/pet/")
          .then((response) => setPets(response.data))
          .catch((err) => {
            console.error("ops! ocorreu um erro" + err);
          });
      }, []);  

    const createdPost = await createPost({
        nome: "Gabriel Welter",
        idade: 23,
        sexo: "M",
        porte: "M",
        tipo: "G",
        castrado: "N",
        descricao: "descricção do gabriel",
        usuario: 777
    })
    console.log({ createdPost });

    return (
        <div className="p-8 text-white">
            <p>teste</p>

            {/* {posts.map((post) => (
                <p key={post.usuario}>{post.idade}</p>
            ))} */}
        </div>
    )
}