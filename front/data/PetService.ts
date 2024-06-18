import api, { BASE_URL, Problem } from "@/app/App"
import { FileService } from "./FileService"
import { AxiosError } from "axios"

export class PetService {
  tiposPermitidos: Array<string> = ["image/jpeg", "image/png"]
  fileService = new FileService()

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

  async uploadImagemPet(imagemIn: Blob, id: number) {
    if (this.tiposPermitidos.filter(tipo => tipo == imagemIn.type).length <= 0) throw new Problem("Não foi possivel realizar o upload da imagem", "Imagem com tipo inválido. Permitido apenas PNG e JPEG", 400, null, null)
    if(imagemIn.size / (1024*1024) > 1.5) {
      await this.fileService.comprimirImagem(imagemIn, async (comprimida: Blob) => { 
        const form = new FormData()
        form.append("imagem", comprimida)

        return api.post(`${BASE_URL}/api/pet/salvar-imagem/${id}`, form) 
      })
    } else {
      const form = new FormData()
        form.append("imagem", imagemIn)

        return api.post(`${BASE_URL}/api/pet/salvar-imagem/${id}`, form) 
    }
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

  async obterPorId(id: string) {
    return api.get(`${BASE_URL}/api/pet/obter-pet/${id}`).then(res => res.data)
  }

  async obterPetsAdotados() {
    return api.get(`${BASE_URL}/api/pet/obter-pets-adotados`)
  }
}