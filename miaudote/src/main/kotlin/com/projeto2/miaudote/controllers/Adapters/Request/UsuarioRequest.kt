package com.projeto2.miaudote.controllers.Adapters.Request

import com.projeto2.miaudote.entities.Usuario
import org.springframework.security.crypto.password.PasswordEncoder

class UsuarioCreate(
    val nome: String,
    val sobrenome: String,
    val username: String,
    val senha: String,
    val email: String,
    val cpf: String,
    val descricao: String?,
    val contato: String?,
) {
    fun toUsuario(passwordEncoder: PasswordEncoder): Usuario {
        return Usuario(
            nome = this.nome,
            sobrenome = sobrenome,
            username = username,
            email = email,
            senha = passwordEncoder.encode(senha),
            cpf = cpf,
            descricao = descricao,
            contato = contato,
            emailVerificado = false,
            perfilAcesso = "A",
            id = null,
            endereco = null
        )
    }
}