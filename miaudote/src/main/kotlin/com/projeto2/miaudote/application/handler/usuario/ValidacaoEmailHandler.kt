package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.EmailService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.application.services.ValidacaoEmailService
import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.domain.entities.ValidacaoEmail
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*
/**
 * Processor responsável por validar o email de um usuário.
 *
 * Este processador realiza a verificação de email com base em um ID de validação, e, se bem-sucedido, marca o email do usuário
 * como verificado. Caso contrário, retorna um erro.
 *
 * @param service Serviço responsável pela validação do email.
 * @param usuarioService Serviço responsável pela manipulação dos dados do usuário.
 */
@Component
class ValidacaoEmailProcessor(
    private val service: ValidacaoEmailService,
    private val usuarioService: UsuarioService,
) : ProcessorHandler<ValidacaoEmailHandler>() {
    /**
     * Processa a validação do email de um usuário.
     *
     * Este método busca a verificação de email pelo ID e, se encontrada, valida o email do usuário. Caso o email já esteja
     * verificado ou o ID de validação não seja encontrado, um erro é retornado.
     *
     * @param handler Dados necessários para realizar a validação do email.
     * @return Resultado da operação, incluindo sucesso ou erro na validação do email.
     */
    override fun process(handler: ValidacaoEmailHandler): Result<Any> {
        val validacao = service.obterPorId(handler.verificacaoId) ?: return Result.success(
            "Verificação não encontrada!!!")
        val usuario = usuarioService.obterUsername(validacao.username) ?: return Result.failure(
            verificacaoEmailProblem("Usuário não encontrado.", "username")
        )
        val usuarioAtualizado = usuario.copy(emailVerificado = true)

        service.deletar(validacao.id!!)

        usuarioService.atualizar(usuarioAtualizado)

        return Result.success("Email verificado com sucesso!!!")
    }
}

/**
 * Classe de handler que contém o ID de verificação de email para validar o email do usuário.
 *
 * Este handler armazena o ID de verificação que será utilizado para validar o email do usuário correspondente.
 *
 * @param verificacaoId ID único associado à verificação do email.
 */
class ValidacaoEmailHandler private constructor(
    val verificacaoId: UUID,
) : RequestHandler {
    companion object {
        /**
         * Cria um novo handler para a validação do email ou retorna um erro caso o ID seja inválido.
         *
         * Este método valida o ID de verificação do email, garantindo que a validação seja realizada corretamente.
         *
         * @param verificacaoIdIn ID de verificação do email fornecido.
         * @return Resultado contendo o handler para a validação do email ou erro de validação.
         */
        fun newOrProblem(
            verificacaoIdIn: String
        ): Result<ValidacaoEmailHandler> {
            val verificacaoId =  try {
                UUID.fromString(verificacaoIdIn)
            } catch (e: Exception){
                return Result.failure(
                    verificacaoEmailProblem("Id da validacao do email inválida.", verificacaoIdIn)
                )
            }
            return Result.success(
                ValidacaoEmailHandler(
                    verificacaoId = verificacaoId)
            )
        }
    }
}

/**
 * Cria um problema com detalhes sobre a falha ao tentar validar o email.
 *
 * Este método cria um problema que será retornado quando ocorrer algum erro na validação do email, como um ID de verificação inválido.
 *
 * @param detalhe Descrição detalhada do erro ocorrido.
 * @param campo Campo que causou o erro.
 * @param valor Valor do campo que causou o erro.
 * @return O problema gerado com os detalhes do erro.
 */
private fun verificacaoEmailProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel validar o email",
    detail = detalhe,
    type = URI("/verificar-email"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)