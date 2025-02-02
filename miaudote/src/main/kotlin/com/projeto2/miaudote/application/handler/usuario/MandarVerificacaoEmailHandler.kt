package com.projeto2.miaudote.application.handler.usuario

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.usuario.UsuarioService
import com.projeto2.miaudote.application.services.usuario.ValidacaoEmailService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI

/**
 * Processor responsável por enviar o email de verificação para o usuário.
 *
 * Este processador verifica se o usuário existe e se o seu email já foi verificado. Caso o email não tenha sido verificado,
 * o processador envia um email de verificação. Caso contrário, retorna uma mensagem indicando que o email já foi verificado.
 *
 * @param service Serviço responsável por enviar o email de verificação.
 * @param usuarioService Serviço responsável por gerenciar os usuários.
 */
@Component
class MandarVerificacaoEmailProcessor(
    private val service: ValidacaoEmailService,
    private val usuarioService: UsuarioService,
) : ProcessorHandler<MandarVerificacaoEmailHandler>() {
    /**
     * Processa a solicitação de envio do email de verificação.
     *
     * Este método verifica a existência do usuário e se o email já foi verificado. Se necessário, envia o email de verificação.
     *
     * @param handler Dados necessários para identificar o usuário e enviar o email de verificação.
     * @return Resultado da operação, incluindo uma mensagem de sucesso ou erro.
     */
    override fun process(handler: MandarVerificacaoEmailHandler): Result<Any> {
        val usuario = usuarioService.obterPorId(handler.id) ?: return Result.failure(
            mandarEmailProblem("Usuário não encontrado.", "id", handler.id.toString())
        )
        if(usuario.emailVerificado){
            return Result.success("Email já verificado")
        }
        service.mandarEmailVerificacao(usuario)

        return Result.success("Email mandado com sucesso!!!")
    }
}
/**
 * Classe de handler que contém o ID do usuário para o envio do email de verificação.
 *
 * Este handler armazena o ID do usuário para que o email de verificação possa ser enviado ao usuário correspondente.
 *
 * @param id ID do usuário que irá receber o email de verificação.
 */
class MandarVerificacaoEmailHandler private constructor(
    val id: Long
) : RequestHandler {
    companion object {

        /**
         * Cria um novo handler para o envio do email de verificação ou retorna um erro caso o ID não seja válido.
         *
         * Este método valida o ID do usuário para garantir que o envio do email de verificação seja realizado corretamente.
         *
         * @param token Token de autenticação contendo o ID do usuário que deve receber o email de verificação.
         * @return Resultado contendo o handler para o envio do email de verificação ou erro de validação.
         */
        fun newOrProblem(token: JwtAuthenticationToken): Result<MandarVerificacaoEmailHandler> {
            val id = token.name.toLongOrNull() ?: return Result.failure(
                mandarEmailProblem(
                    "Id do usuário não encontrado.",
                    "ID",
                )
            )
            return Result.success(
                MandarVerificacaoEmailHandler(id)
            )
        }
    }
}
/**
 * Cria um problema com detalhes sobre a falha ao tentar enviar o email de verificação.
 *
 * Este método cria um problema que será retornado quando ocorrer algum erro ao tentar enviar o email de verificação,
 * como a inexistência do ID do usuário.
 *
 * @param detalhe Descrição detalhada do erro ocorrido.
 * @param campo Campo que causou o erro.
 * @param valor Valor do campo que causou o erro.
 * @return O problema gerado com os detalhes do erro.
 */
private fun mandarEmailProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel mandar o email de validacao",
    detail = detalhe,
    type = URI("/verificar-email"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)