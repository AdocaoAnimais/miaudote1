package com.projeto2.miaudote.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.projeto2.miaudote.application.problems.Problem
import org.springframework.http.HttpStatus
import java.net.URI
/**
 * Enum que representa os diferentes sexos de um animal.
 *
 * Contém os valores possíveis para o sexo de um animal:
 * - "F" para Fêmea
 * - "M" para Macho
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Sexo(val id: String, val nome: String) {
    F("F", "Fêmea"),
    M("M", "Macho")
    ;
}

/**
 * Converte uma string para um valor da enumeração [Sexo].
 *
 * Se a string corresponder a um valor válido, retorna o valor da enumeração correspondente.
 * Caso contrário, retorna um erro indicando que o valor é inválido.
 *
 * @return Um [Result] contendo o valor da enumeração ou um erro se o valor for inválido.
 */
fun String.toSexo(): Result<Sexo> {
    val sexo = Sexo.entries.find { it.id == this}
    sexo ?: return Result.failure(
        sexoInvalido(
            "Valor invalido para Sexo",
        )
    )
    return Result.success(sexo)
}

/**
 * Cria um problema de erro para quando o valor de 'sexo' é inválido.
 *
 * @param detalhe Detalhe adicional sobre o erro.
 * @return Um objeto [Problem] representando o erro.
 */
private fun sexoInvalido(detalhe: String) = Problem(
    title = "Valor para 'sexo' invalido",
    detail = detalhe,
    type = URI("/sexo"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)