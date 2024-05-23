package com.projeto2.miaudote.apresentation.Response

import com.projeto2.miaudote.domain.entities.Usuario

class UsuarioCreateResponse(
    val username: String,
    val token: String,
)

fun Usuario.fromEntity(token: String): UsuarioCreateResponse {
    return UsuarioCreateResponse(
        username = this.username,
        token = token
    )
}
