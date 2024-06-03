package com.projeto2.miaudote.apresentation.Request

import org.springframework.web.multipart.MultipartFile

data class PetCreate(
    val nome: String?,
    val sexo: String?,
    val porte: String?,
    val idade: String?,
    val tipo: String?,
    val castrado: String?,
    val descricao: String?
//    val imagem: MultipartFile?
)
data class PetUpdate(
    val nome: String?,
    val sexo: String?,
    val porte: String?,
    val idade: String?,
    val tipo: String?,
    val castrado: String?,
    val descricao: String?
//    val imagem: MultipartFile?
)