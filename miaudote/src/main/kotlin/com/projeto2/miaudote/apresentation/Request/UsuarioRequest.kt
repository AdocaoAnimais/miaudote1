package com.projeto2.miaudote.apresentation.Request

class UsuarioCreate(
    val nome: String?,
    val sobrenome: String?,
    val username: String?,
    val senha: String?,
    val email: String?,
    val cpf: String?,
    val descricao: String?,
    val contato: String?,
    val endereco: String?,
)

class UsuarioUpdate(
    val nome: String?,
    val sobrenome: String?,
    val username: String?,
    val email: String?,
    val cpf: String?,
    val descricao: String?,
    val contato: String?,
    val edereco: String?,
    val senha: String?,
)