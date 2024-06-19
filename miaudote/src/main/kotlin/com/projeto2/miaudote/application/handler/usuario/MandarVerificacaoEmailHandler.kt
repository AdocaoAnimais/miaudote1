package com.projeto2.miaudote.application.handler.usuario

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
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
class MandarVerificacaoEmailProcessor(
    private val service: ValidacaoEmailService,
    private val usuarioService: UsuarioService,
) : ProcessorHandler<MandarVerificacaoEmailHandler>() {

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

class MandarVerificacaoEmailHandler private constructor(
    val id: Long
) : RequestHandler {
    companion object {
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

private fun mandarEmailProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel mandar o email de validacao",
    detail = detalhe,
    type = URI("/verificar-email"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)