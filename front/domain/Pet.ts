export interface PetPost {
  id: number,
  nome: string,
  castrado: {
    nome: string,
    id: string
  },
  sexo: {
    nome: string,
    id: string
  },
  porte: {
    nome: string,
    id: string
  },
  tipo: {
    nome: string,
    id: string
  },
  descricao: string,
  idade: number,
  idUsuario: number
}