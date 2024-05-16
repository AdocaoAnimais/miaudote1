package com.projeto2.miaudote.controllers.Adapters.Request

import com.projeto2.miaudote.entities.Pet
import com.projeto2.miaudote.enums.* 
import java.time.LocalDateTime

interface PetRequest {
    fun toPet(): Pet
}

class PetCreate (
    val nome: String?,
    val sexo: String?,
    val porte: String?,
    val idade: String?,
    val tipo: String?,
    val castrado: String?,
    val descricao: String?,
    val usuario: String?,
): PetRequest {
    override fun toPet(): Pet {
        this.nome ?: error("Nome não pode ser null")
        val sexoIn = sexo?.toSexo() ?: error("Sexo não pode ser null")
        val usuarioIn = usuario?.toLongOrNull() ?: error("Id do usuário invalido")
        val castradoIn = castrado?.toCastrado()
        val pet = Pet(
            nome = nome,
            idade = idade?.toIntOrNull(),
            porte = porte?.toPorte(),
            sexo = sexoIn,
            tipo = tipo?.toTipo(),
            castrado = castradoIn,
            descricao = descricao,
            idUsuario = usuarioIn,
            dataCadastro = LocalDateTime.now(),
            id = null
        )
        return pet
    }
}