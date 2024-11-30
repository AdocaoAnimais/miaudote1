package com.projeto2.miaudote.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.projeto2.miaudote.application.problems.Problem
import org.springframework.http.HttpStatus
import java.net.URI
/**
 * Enumeração que representa os tipos de pet.
 *
 * @property id Identificador único do tipo de pet.
 * @property nome Nome do tipo de pet.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Tipo(val id: String, val nome: String) {
    C("C", "Cachorro"),
    G("G", "Gato")
    ;
}
/**
 * Converte uma string para um tipo de pet (Tipo).
 *
 * @param tipoId O identificador do tipo de pet a ser convertido.
 * @return Um resultado com o tipo de pet correspondente ou um erro se o valor for inválido.
 */
fun String.toTipo(): Result<Tipo> {
    val tipo = Tipo.entries.find { it.id == this}
    tipo ?: return Result.failure(
        tipoInvalido("")
    )
    return Result.success(tipo)
}
/**
 * Cria um objeto Problem indicando que o valor do tipo de pet é inválido.
 *
 * @param detalhe Detalhes adicionais sobre o erro.
 * @return Um objeto Problem com os detalhes do erro.
 */
private fun tipoInvalido(detalhe: String) = Problem(
    title = "Valor para 'tipo' invalido",
    detail = detalhe,
    type = URI("/tipo"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)