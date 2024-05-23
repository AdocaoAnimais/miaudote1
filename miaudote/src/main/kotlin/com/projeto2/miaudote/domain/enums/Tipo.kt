package com.projeto2.miaudote.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.projeto2.miaudote.application.problems.Problem
import org.springframework.http.HttpStatus
import java.net.URI

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Tipo(val id: String, val nome: String) {
    C("C", "Cachorro"),
    G("G", "Gato")
    ;
}

fun String.toTipo(): Result<Tipo> {
    val tipo = Tipo.entries.find { it.id == this}
    tipo ?: return Result.failure(
        tipoInvalido("")
    )
    return Result.success(tipo)
}

private fun tipoInvalido(detalhe: String) = Problem(
    title = "Valor para 'tipo' invalido",
    detail = detalhe,
    type = URI("/tipo"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)