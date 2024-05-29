package com.projeto2.miaudote.apresentation.Request

data class PetCreate(
    val nome: String?,
    val sexo: String?,
    val porte: String?,
    val idade: String?,
    val tipo: String?,
    val castrado: String?,
    val descricao: String?,
)
data class PetUpdate(
    val nome: String?,
    val sexo: String?,
    val porte: String?,
    val idade: String?,
    val tipo: String?,
    val castrado: String?,
    val descricao: String?,
)