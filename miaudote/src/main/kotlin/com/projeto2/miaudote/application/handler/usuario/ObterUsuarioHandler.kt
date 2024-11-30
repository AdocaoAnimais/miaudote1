package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.apresentation.Response.UsuarioResponse
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI
/**
 * Processor responsável por obter as informações de um usuário.
 *
 * Este processador busca os dados do usuário com base no ID fornecido e os retorna como uma resposta. Caso o usuário
 * não seja encontrado, um erro é retornado.
 *
 * @param service Serviço responsável por gerenciar os usuários e realizar a busca por ID.
 */
@Component
class ObterUsuarioProcessor(
    private val service: UsuarioService,
) : ProcessorHandler<ObterUsuarioHandler>() {
    /**
     * Processa a solicitação para obter as informações de um usuário.
     *
     * Este método busca o usuário pelo ID e, caso encontrado, retorna suas informações. Caso contrário, retorna um erro.
     *
     * @param handler Dados necessários para identificar o usuário que deve ser recuperado.
     * @return Resultado da operação, incluindo as informações do usuário ou um erro caso o usuário não seja encontrado.
     */
    override fun process(handler: ObterUsuarioHandler): Result<Any> {
        val usuario = service.obterPorId(handler.id).toProblem().getOrElse { return Result.failure(it) }
        val response = UsuarioResponse(
            id = usuario.id,
            username = usuario.username,
            nome = usuario.nome,
            sobrenome = usuario.sobrenome,
            email = usuario.email,
            cpf = usuario.cpf,
            descricao = usuario.descricao,
            contato = usuario.contato,
            endereco = usuario.endereco,
            email_verificado = usuario.emailVerificado,
        )
        return Result.success(response)
    }
}


/**
 * Classe de handler que contém o ID do usuário para recuperar as suas informações.
 *
 * Este handler armazena o ID do usuário que será utilizado para buscar as informações do usuário correspondente.
 *
 * @param id ID do usuário cujas informações serão recuperadas.
 */
class ObterUsuarioHandler private constructor(
    val id: Long,
) : RequestHandler {
    companion object {
        /**
         * Cria um novo handler para obter as informações do usuário ou retorna um erro caso o ID não seja válido.
         *
         * Este método valida o ID do usuário, garantindo que a solicitação de obtenção de informações seja realizada corretamente.
         *
         * @param token Token de autenticação contendo o ID do usuário cujas informações devem ser recuperadas.
         * @return Resultado contendo o handler para a obtenção das informações ou erro de validação.
         */
        fun newOrProblem(token: JwtAuthenticationToken): Result<ObterUsuarioHandler> {
            // You can perform any necessary initialization or validation here
            // For simplicity, let's assume no specific initialization is needed
            val id = token.name.toLongOrNull() ?: return Result.failure(
                obterProblemaUsuario(
                    "Id do usuário invalido.",
                    "ID",
                )
            )
            return Result.success(ObterUsuarioHandler(id))
        }
    }
}

/**
 * Cria um problema com detalhes sobre a falha ao tentar obter as informações do usuário.
 *
 * Este método cria um problema que será retornado quando ocorrer algum erro ao tentar obter as informações do usuário,
 * como um ID inválido ou ausente.
 *
 * @param detalhe Descrição detalhada do erro ocorrido.
 * @param campo Campo que causou o erro.
 * @param valor Valor do campo que causou o erro.
 * @return O problema gerado com os detalhes do erro.
 */
private fun obterProblemaUsuario(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel obter informações do usuario",
    detail = detalhe,
    type = URI("/obter-usuario"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)