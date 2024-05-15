package com.projeto2.miaudote.enums

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Sexo(val id: String, val nome: String) {
    F("F", "FEMININO"),
    M("M", "MASCULINO")
    ;
}

fun String.toSexo(): Sexo {
    val sexo = Sexo.entries.find { it.id == this}
    sexo ?: error("Sexo não encontrado.")
    return sexo
}