package com.projeto2.miaudote.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.projeto2.miaudote.application.problems.Problem
import org.springframework.http.HttpStatus
import java.net.URI

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Castrado(val id: String, val nome: String) {
    C("C", "Castrado"),
    N("N", "Não castrado"),
    ;
}

fun String.toCastrado(): Result<Castrado> {
    val castrado = Castrado.entries.find { it.id == this }
     castrado ?: return Result.failure(
         castradoInvalido("")
     )
    return Result.success(castrado)
}

private fun castradoInvalido(detalhe: String) = Problem(
    title = "Valor para 'castrado' invalido",
    detail = detalhe,
    type = URI("/castrado"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)