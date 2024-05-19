package com.projeto2.miaudote.apresentation.Adapters.Response

import com.projeto2.miaudote.entities.Usuario

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
