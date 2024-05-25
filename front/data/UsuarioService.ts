import api, { BASE_URL } from "@/app/App";
import { LoginResponse } from "@/domain/Login";

export class UsuarioService {

    async cadastrar(
        nome: string,
        sobrenome: string,
        username: string,
        senha: string,
        email: string,
        cpf: string,
        contato: string,
        endereco: string
    ) {
        const params ={
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
        await api.post(`${BASE_URL}/api/usuario/cadastrar`, params).then(res => { 
                let response = res.data as LoginResponse 
                console.log(res)
                api.defaults.headers.common['Authorization'] = `Bearer ${response.accessToken}`;
        }).catch((error) => {
            console.log(error)
        });
    }
}