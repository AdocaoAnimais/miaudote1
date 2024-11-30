package com.projeto2.miaudote.application.problems

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import java.net.URI
/**
 * Representa um problema que ocorre durante o processamento de uma requisição.
 *
 * Esta classe armazena informações sobre um erro ou falha, incluindo um título, detalhes, status HTTP, tipo (URI) e propriedades adicionais.
 *
 * @param title Título do problema, que descreve brevemente o erro.
 * @param detail Detalhes adicionais sobre o erro ocorrido.
 * @param status Código de status HTTP que descreve o tipo de erro.
 * @param type URI que representa o tipo ou categoria do erro.
 * @param extra Propriedades adicionais relacionadas ao erro, representadas como um mapa de chave-valor.
 */
class Problem(
    val title: String,
    val detail: String,
    val status: Int,
    val type: URI,
    val extra: Map<String, Any?>?,
) : Exception() {
    /**
     * Constrói um problema com status HTTP usando um valor de `HttpStatus`.
     *
     * Esta sobrecarga do construtor permite passar um objeto `HttpStatus` diretamente para especificar o status da falha.
     *
     * @param title Título do problema.
     * @param detail Descrição do erro ocorrido.
     * @param status Status HTTP que representa o tipo de erro.
     * @param type URI que identifica o tipo do erro.
     * @param extra Propriedades adicionais relacionadas ao erro.
     */
    constructor(
        title: String,
        detail: String,
        status: HttpStatus,
        type: URI,
        extra: Map<String, String?>?,
    ) : this(
        title = title,
        detail = detail,
        status = status.value(),
        type = type,
        extra = extra
    )
    /**
     * Obtém os detalhes do problema em um formato específico.
     *
     * Este método cria um objeto `ProblemDetail` com base no problema atual, preenchendo seus campos com as informações da classe `Problem`.
     *
     * @return Um objeto `ProblemDetail` com os detalhes do problema.
     */
    fun getProblemDetail(): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, this.title)
        problem.detail = this.detail
        problem.title = this.title
        problem.type = this.type
        problem.status = this.status
        problem.properties = this.extra
        return problem
    }
}

/**
 * Converte o problema atual em uma falha de resultado (failure).
 *
 * Este método converte o problema em um objeto `Result` com falha, que pode ser utilizado em operações de processamento de requisições.
 *
 * @return Um objeto `Result` que contém a falha representada pelo problema atual.
 */
fun Problem.toFailure(): Result<Any> = Result.failure(this)