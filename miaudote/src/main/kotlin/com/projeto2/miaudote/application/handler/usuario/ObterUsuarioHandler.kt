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

@Component
class ObterUsuarioProcessor(
    private val service: UsuarioService,
) : ProcessorHandler<ObterUsuarioHandler>() {
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
            contato = usuario.contato
        )
        return Result.success(response)
    }
}


class ObterUsuarioHandler private constructor(
    val id: Long,
) : RequestHandler {
    companion object {
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

private fun obterProblemaUsuario(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel obter informações do usuario",
    detail = detalhe,
    type = URI("/obter-usuario"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)