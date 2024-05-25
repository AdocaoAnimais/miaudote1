import api, { BASE_URL } from "@/app/App";
import { LoginResponse } from "@/domain/Login";

export class AuthenticationService {
    async loggin(username: string, senha: string) {
        const params = {
            "username": username,
            "senha": senha
        }

        await api.post(`${BASE_URL}/api/auth/login`, params).then(res => {
            let response = res.data as LoginResponse 
            api.defaults.headers.common['Authorization'] = `Bearer ${response.accessToken}`; 
        })
    }

    async logged(): Promise<boolean> { 
        return await api.get(`${BASE_URL}/api/auth/logged`)
            .then(res => true)
            .catch((error) => false)
    }

    logout() { 
        api.defaults.headers.common['Authorization'] = null;
    }
}