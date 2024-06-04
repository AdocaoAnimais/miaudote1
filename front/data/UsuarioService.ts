import api, { BASE_URL } from "@/app/App";
import { LoginResponse } from "@/domain/Login";

export class UsuarioService {

  async cadastrar(
    nome: string,
    sobrenome: string,
    username: string,
    email: string,
    senha: string,
    cpf: string,
    contato: string,
    endereco: string
  ) {
    const params = {
      "nome": nome,
      "sobrenome": sobrenome,
      "username": username,
      "email": email,
      "senha": senha,
      "cpf": cpf,
      "contato": contato,
      "endereco": endereco,
      "descricao": "",
    }
    console.log(params)
    return await api.post(`${BASE_URL}/api/usuario/cadastrar`, params).then(res => {
      let response = res.data as LoginResponse 
      api.defaults.headers.common['Authorization'] = `Bearer ${response.accessToken}`;
    }).catch((error) => {throw error});
  }

  async obter() {
    return await api.get(`${BASE_URL}/api/usuario/obter`)
  }
}