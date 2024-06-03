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

  async uploadImagemPet(imagem: any, id: number){
    const form = new FormData()
    form.append("imagem", imagem)

    api.post(`${BASE_URL}/api/pet/salvar-imagem/${id}`, form).then((res) => console.log(res)).catch(error => console.log(error))
  }

  async obterTodosPet() {
    return api
      .get(`${BASE_URL}/api/pet/obter-pets`)
      .then((response) => response.data)
      .catch((err) => {
        console.error("ops! ocorreu um erro" + err);
      });
  }

  async obterPetsUsuario() {
    return api
      .get(`${BASE_URL}/api/pet/obter-pets-usuario`)
  }
}