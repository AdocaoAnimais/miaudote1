package com.projeto2.miaudote.apresentation.Response

import com.projeto2.miaudote.domain.entities.Usuario

class UsuarioCreateResponse(
    val username: String,
    val token: String,
)
class UsuarioResponse(
    val username: String,
    val id: Long?,
    val nome: String?,
    val sobrenome: String?,
    val email: String?,
    val cpf: String?,
    val descricao: String?,
    val contato: String?,
)

fun Usuario.fromEntity(token: String): UsuarioCreateResponse {
    return UsuarioCreateResponse(
        username = this.username,
        token = token
    )
}
