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

@Component
class ValidacaoEmailProcessor(
    private val service: ValidacaoEmailService,
    private val usuarioService: UsuarioService,
) : ProcessorHandler<ValidacaoEmailHandler>() {

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

class ValidacaoEmailHandler private constructor(
    val verificacaoId: UUID,
) : RequestHandler {
    companion object {
        fun newOrProblem(
            verificacaoIdIn: String
        ): Result<ValidacaoEmailHandler> {
            val verificacaoId = UUID.fromString(verificacaoIdIn) ?: return Result.failure(
                verificacaoEmailProblem("Id da validacao do email inválida.", verificacaoIdIn)
            )
            return Result.success(
                ValidacaoEmailHandler(
                    verificacaoId = verificacaoId)
            )
        }
    }
}

private fun verificacaoEmailProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel validar o email",
    detail = detalhe,
    type = URI("/verificar-email"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)