package com.projeto2.miaudote.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.projeto2.miaudote.application.problems.Problem
import org.springframework.http.HttpStatus
import java.net.URI
/**
 * Enum que representa os diferentes portes de animais.
 *
 * Contém os valores possíveis para o porte de um animal, com seus respectivos
 * identificadores, nomes e descrições de tamanhos:
 * - "P" para Pequeno
 * - "M" para Médio
 * - "G" para Grande
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Porte (val id: String, val nome: String, val descricao: String) {
    P("P", "Pequeno", "Temanho x até Y"),
    M("M", "Médio", "Tamanho Y+1 até Z"),
    G("G", "Grande", "Tamanho Z+1 até A")
    ;
}
/**
 * Converte uma string para um valor da enumeração [Porte].
 *
 * Se a string corresponder a um valor válido, retorna o valor da enumeração correspondente.
 * Caso contrário, retorna um erro indicando que o valor é inválido.
 *
 * @return Um [Result] contendo o valor da enumeração ou um erro se o valor for inválido.
 */
fun String.toPorte(): Result<Porte> {
    val porte = Porte.entries.find { it.id == this }
    porte ?: return Result.failure(
        porteInvalido(
            "Valor invalido para Porte",
        )
    )
    return Result.success(porte)
}

/**
 * Cria um problema de erro para quando o valor de 'porte' é inválido.
 *
 * @param detalhe Detalhe adicional sobre o erro.
 * @return Um objeto [Problem] representando o erro.
 */
private fun porteInvalido(detalhe: String) = Problem(
    title = "Valor para 'porte' invalido",
    detail = detalhe,
    type = URI("/porte"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)