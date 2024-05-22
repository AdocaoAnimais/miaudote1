package com.projeto2.miaudote.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.projeto2.miaudote.application.problems.Problem
import org.springframework.http.HttpStatus
import java.net.URI

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Porte (val id: String, val nome: String, val descricao: String) {
    P("P", "Pequeno", "Temanho x até Y"),
    M("M", "Médio", "Tamanho Y+1 até Z"),
    G("G", "Grande", "Tamanho Z+1 até A")
    ;
}

fun String.toPorte(): Result<Porte> {
    val porte = Porte.entries.find { it.id == this }
    porte ?: return Result.failure(
        porteInvalido(
            "Valor invalido para Porte",
        )
    )
    return Result.success(porte)
}

private fun porteInvalido(detalhe: String) = Problem(
    title = "Valor para 'porte' invalido",
    detail = detalhe,
    type = URI("/porte"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)