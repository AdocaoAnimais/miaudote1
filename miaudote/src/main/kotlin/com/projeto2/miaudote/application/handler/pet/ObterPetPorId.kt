package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.PetService
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI

@Component
class ObterPetPorIdProcessor(
    private val service: PetService,
) : ProcessorHandler<ObterPetPorIdHandler>() {
    override fun process(handler: ObterPetPorIdHandler): Result<Any> {
        val response = service.obterPorId(handler.id)
        return Result.success(response) as Result<Any>
    }
}

class ObterPetPorIdHandler private constructor(
    val id: Long
) : RequestHandler {
    companion object {
        fun newOrProblem(idPet: Long, token: JwtAuthenticationToken): Result<ObterPetPorIdHandler> {
            token.name.toLongOrNull() ?: return Result.failure(
                Problem(
                    title = "Não foi possivel obter o pet",
                    detail = "Id do usuário inválido.",
                    type = URI("/obter-pet-por-id"),
                    status = HttpStatus.BAD_REQUEST,
                    extra = null
                )
            )

            return Result.success(ObterPetPorIdHandler(idPet))
        }
    }
}
