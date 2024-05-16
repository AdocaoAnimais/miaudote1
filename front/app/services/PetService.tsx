import axios, { AxiosResponse } from "axios";
import api from "../App";

export interface PetPost {
    id: number,
    nome: string,
    sexo: string,
    idade: number,
    porte: {
        id: string,
        nome: string,
        descricao: string,
    },
    castrado: string,
    descricao: string,
    foto: string,
    tipo: {
        id: string,
        nome: string,
        descricao: string,
    },
    idUsuario: number,
    dataCadastro: string,
}


export let TOKEN = "";

export async function loginUser(username: string, senha: string) {
    const params = {
        "username": username,
        "senha": senha
    }

    await api.post("http://localhost:8080/api/auth/login", params).then(res => {
        TOKEN = res.data.accessToken
    })
}

export async function createPet(nome: string,
    idade: string,
    sexo: string,
    porte: string,
    tipo: string,
    castrado: string,
    descricao: string,
) {
    const token = window.localStorage.getItem("tokenMiaudote")
    console.log(token)
    api.defaults.headers.common.Authorization = `${token}`
    const params = {
        "nome": nome,
        "sexo": sexo,
        "porte": porte,
        "idade": idade,
        "tipo": tipo,
        "castrado": castrado,
        "descricao": descricao
    }
    api.post("http://localhost:8080/api/pet/", params)
    .then(res => {
        console.log(res)
    })


}