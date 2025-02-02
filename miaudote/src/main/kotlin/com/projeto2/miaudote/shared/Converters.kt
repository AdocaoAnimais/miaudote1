package com.projeto2.miaudote.shared

import com.projeto2.miaudote.application.problems.Problem
import org.springframework.http.HttpStatus
import java.net.URI
import java.util.*


fun String?.toUUID(): Result<UUID> {
    return try {
        Result.success(UUID.fromString(this))
    } catch (e: Exception) {
        Result.failure(
            convertersProblem(
                "UUID inválido.",
                "/uuid-to-string"

            )
        )
    }
}

fun convertersProblem(descricao: String, uri: String, extra: Map<String, String>? = null): Problem {
    return Problem(
        title = "Erro ao converter valores",
        detail = descricao,
        type = URI("/converters${uri}"),
        status = HttpStatus.BAD_REQUEST,
        extra = extra,
    )
}