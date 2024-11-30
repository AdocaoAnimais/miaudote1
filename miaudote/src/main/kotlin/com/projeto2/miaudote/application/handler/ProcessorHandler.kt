package com.projeto2.miaudote.application.handler

import org.springframework.stereotype.Service
/**
 * Classe abstrata que define o processamento de requisições para um tipo específico de manipulador.
 *
 * Esta classe serve como base para processadores que lidam com diferentes tipos de requisições, implementando o método
 * `process` que processa o manipulador de requisição e retorna um resultado.
 *
 * @param T Tipo específico de `RequestHandler` que será processado por esta classe.
 */
@Service
abstract class ProcessorHandler<T: RequestHandler> {
    /**
     * Método abstrato para processar uma requisição.
     *
     * Este método deve ser implementado para lidar com a lógica de processamento de um manipulador específico,
     * retornando um resultado com base na requisição fornecida.
     *
     * @param handler O manipulador de requisição a ser processado.
     * @return Um objeto `Result` que representa o resultado do processamento da requisição.
     */
    abstract fun process(handler: T): Result<Any>
}