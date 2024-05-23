package com.projeto2.miaudote.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.projeto2.miaudote.application.problems.Problem
import org.springframework.http.HttpStatus
import java.net.URI

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Sexo(val id: String, val nome: String) {
    F("F", "Feminimo"),
    M("M", "Masculino")
    ;
}

fun String.toSexo(): Result<Sexo> {
    val sexo = Sexo.entries.find { it.id == this}
    sexo ?: return Result.failure(
        sexoInvalido(
            "Valor invalido para Sexo",
        )
    )
    return Result.success(sexo)
}

private fun sexoInvalido(detalhe: String) = Problem(
    title = "Valor para 'sexo' invalido",
    detail = detalhe,
    type = URI("/sexo"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)