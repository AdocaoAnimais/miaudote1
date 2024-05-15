import axios, { AxiosResponse } from "axios";

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
    foto: Blob, 
    tipo: {
        id: string,
        nome: string,
        descricao: string,
    },
    idUsuario: number,
    dataCadastro: string,
}
 