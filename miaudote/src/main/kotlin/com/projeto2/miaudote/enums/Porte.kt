package com.projeto2.miaudote.enums

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Porte (val id: String, val nome: String, val descricao: String) {
    P("P", "Pequeno", "Temanho x até Y"),
    M("M", "Médio", "Tamanho Y+1 até Z"),
    G("G", "Grande", "Tamanho Z+1 até A")
    ;
}

fun String.toPorte(): Porte {
    val raca = Porte.entries.find { it.id == this }
    raca ?: error("Porte não encontrado.")
    return raca
}