package com.projeto2.miaudote.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.projeto2.miaudote.application.problems.Problem
import org.springframework.http.HttpStatus
import java.net.URI
/**
 * Enum que representa o status de castração de um animal.
 *
 * Contém os valores possíveis para o status de castração:
 * - "C" para Castrado
 * - "N" para Não castrado
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Castrado(val id: String, val nome: String) {
    C("C", "Castrado"),
    N("N", "Não castrado"),
    ;
}

/**
 * Converte uma string para um valor da enumeração [Castrado].
 *
 * Se a string corresponder a um valor válido, retorna o valor da enumeração correspondente.
 * Caso contrário, retorna um erro indicando que o valor é inválido.
 *
 * @return Um [Result] contendo o valor da enumeração ou um erro se o valor for inválido.
 */
fun String.toCastrado(): Result<Castrado> {
    val castrado = Castrado.entries.find { it.id == this }
     castrado ?: return Result.failure(
         castradoInvalido("")
     )
    return Result.success(castrado)
}
/**
 * Cria um problema de erro para quando o valor de 'castrado' é inválido.
 *
 * @param detalhe Detalhe adicional sobre o erro.
 * @return Um objeto [Problem] representando o erro.
 */
private fun castradoInvalido(detalhe: String) = Problem(
    title = "Valor para 'castrado' invalido",
    detail = detalhe,
    type = URI("/castrado"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)