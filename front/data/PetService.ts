import api, { BASE_URL } from "@/app/App"

export class PetService {
    async createPet(nome: string,
        idade: string,
        sexo: string,
        porte: string,
        tipo: string,
        castrado: string,
        descricao: string,
    ) {
        const params = {
            "nome": nome,
            "sexo": sexo,
            "porte": porte,
            "idade": idade,
            "tipo": tipo,
            "castrado": castrado,
            "descricao": descricao
        }
        return api.post(`${BASE_URL}/api/pet/`, params)
    }
}