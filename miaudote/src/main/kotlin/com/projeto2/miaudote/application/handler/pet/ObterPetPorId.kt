package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI

@Component
class ObterPetPorIdProcessor(
    private val service: PetService,
) : ProcessorHandler<ObterPetPorIdHandler>() {
    override fun process(handler: ObterPetPorIdHandler): Result<Any> {
        val pet = service.obterPorId(handler.id).toProblem().getOrElse { return Result.failure(it) }
        if(pet.idUsuario != handler.idUsuario) return Result.failure(
            obterPorIdProblem("Usuário não cadastrou o pet.")
        )
        return Result.success(pet)
    }
}

class ObterPetPorIdHandler private constructor(
    val id: Long,
    val idUsuario: Long,
) : RequestHandler {
    companion object {
        fun newOrProblem(idPet: Long, token: JwtAuthenticationToken): Result<ObterPetPorIdHandler> {
            val idUsuario = token.name.toLongOrNull() ?: return Result.failure(
                obterPorIdProblem("Id do usuário inválido.")
            )

            return Result.success(ObterPetPorIdHandler(idPet, idUsuario))
        }
    }
}

private fun obterPorIdProblem(detail: String) = Problem(
    title = "Não foi possivel obter o pet",
    detail = detail,
    type = URI("/obter-pet-por-id"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)