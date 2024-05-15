package com.projeto2.miaudote.enums

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Castrado(val id: String, val nome: String) {
    C("C", "Castrado"),
    N("N", "Não castrado"),
    ;
}

fun String.toCastrado(): Castrado? {
    val castrado = Castrado.entries.find { it.id == this }
    // castrado ?: error("Informação de castrado não encontrado.")
    return castrado
}