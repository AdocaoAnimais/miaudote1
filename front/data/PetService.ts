import api, { BASE_URL } from "@/app/App"

export class PetService {

  constructor() {

  }

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

  async atualizar(nome: string,
    idade: number,
    sexo: string,
    porte: string,
    tipo: string,
    castrado: string,
    descricao: string,
    id: string,
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
    return await api.post(`${BASE_URL}/api/pet/atualizar/${id}`, params)
  }

  async uploadImagemPet(imagem: any, id: number) {
    const form = new FormData()
    form.append("imagem", imagem)

    return api.post(`${BASE_URL}/api/pet/salvar-imagem/${id}`, form)
      .then((res) => console.log(res))
      .catch(error => { throw error })
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

  async solicitarAdocao(id: string) {
    return api.post(`${BASE_URL}/api/pet/solicitar-adocao/${id}`)
      .then(res => res.data)
  }

  async deletar(id: string) {
    return api.delete(`${BASE_URL}/api/pet/deletar/${id}`)
      .then(res => res.data)
  }
}