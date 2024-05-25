import api from "@/app/App";
import { LoginResponse } from "@/domain/Login";

export class AuthenticationService {
    async loggin(username: string, senha: string) {
        const params = {
            "username": username,
            "senha": senha
        }

        await api.post("http://localhost:8080/api/auth/login", params).then(res => {
            let response = res.data as LoginResponse
            // api.defaults.auth = {
            //     username: response.username,
            //     password: senha
            // }
            api.defaults.headers.common['Authorization'] = `Bearer ${response.accessToken}`; 
        })
    }

    async logged(): Promise<boolean> { 
        return await api.get("http://localhost:8080/api/auth/logged")
            .then(res => true)
            .catch((error) => false)
    }
}