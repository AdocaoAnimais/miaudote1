package com.projeto2.miaudote.enums

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Tipo(val id: String, val nome: String) {
    C("C", "Cachorro"),
    G("G", "Gato")
    ;
}

fun String.toTipo(): Tipo {
    val tipo = Tipo.entries.find { it.id == this}
    tipo ?: error("Tipo não encontrado.")
    return tipo
}