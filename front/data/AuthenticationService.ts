import api, { BASE_URL } from "@/app/App";
import { LoginResponse } from "@/domain/Login";

export class AuthenticationService {
    private chave = "tokenMiaudote"
    async loggin(username: string, senha: string) {
        const params = {
            "username": username,
            "senha": senha
        }
        api.defaults.headers.common['Authorization'] = null
        window.localStorage.removeItem(this.chave)
        await api.post(`${BASE_URL}/api/auth/login`, params).then(res => {
            let response = res.data as LoginResponse 
            window.localStorage.setItem(this.chave, response.accessToken)
            api.defaults.headers.common['Authorization'] = `Bearer ${response.accessToken}`; 
        })
    }

    async logged(): Promise<boolean> { 
        const token = window.localStorage.getItem(this.chave) 
        if(token!=null){
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`; 
        }
        return await api.get(`${BASE_URL}/api/auth/logged`)
            .then(res => true)
            .catch((error) => false)
    }

    logout() { 
        window.localStorage.removeItem(this.chave)
        api.defaults.headers.common['Authorization'] = null;
    }
}