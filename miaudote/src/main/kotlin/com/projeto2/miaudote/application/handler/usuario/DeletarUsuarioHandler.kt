package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.net.URI


@Service
class DeletarUsuarioProcessor(
    private val service: UsuarioService,
) : ProcessorHandler<DeletarUsuarioHandler>() {
    override fun process(handler: DeletarUsuarioHandler): Result<Any> {
        val id = handler.id
        // testar se o usuário de fato existe
        service.obterPorId(id).toProblem().getOrElse { return Result.failure(it) }
        service.deletar(id)
        return Result.success(id)
    }
}

class DeletarUsuarioHandler private constructor(
    val id: Long
) : RequestHandler {
    companion object {
        fun newOrProblem(token: JwtAuthenticationToken): Result<DeletarUsuarioHandler> {
            // You can perform any necessary initialization or validation here
            // For simplicity, let's assume no specific initialization is needed
            val id = token.name.toLongOrNull() ?: return Result.failure(
                deletarProblemaUsuario(
                    "Id do usuário não encontrado.",
                    "ID",
                )
            )
            return Result.success(DeletarUsuarioHandler(id))
        }
    }
}

private fun deletarProblemaUsuario(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel deletar o usuario",
    detail = detalhe,
    type = URI("/deletar-usuario"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)
