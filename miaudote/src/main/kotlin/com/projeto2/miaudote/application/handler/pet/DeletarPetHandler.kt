package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.net.URI


@Service
class DeletarPetProcessor(
    private val service: PetService,
) : ProcessorHandler<DeletarPetHandler>() {
    override fun process(handler: DeletarPetHandler): Result<Any> {
        val usuarioId = handler.token.name.toLongOrNull() ?: return Result.failure(
            deletarPetProblem(
                "Id do usuário não encontrado.",
                "ID",
            )
        )
        val pet = service.obterPorId(handler.petId) ?: return deletarPetProblem(
            "Pet não encontrado",
            "pet",
        ).toFailure()

        if (pet.idUsuario != usuarioId) {
            return deletarPetProblem(
                "Usuário não tem permissão para atualizar este pet",
                "usuarioId",
                usuarioId.toString()
            ).toFailure()
        }
        // testar se o pet de fato existe
        service.obterPorId(handler.petId).toProblem().getOrElse { return Result.failure(it) }
        service.deletar(handler.petId)
        return Result.success(handler.petId)
    }
}

class DeletarPetHandler(
    val petId: Long,
    val token: JwtAuthenticationToken
) : RequestHandler {
    companion object {
        fun newOrProblem(petId: Long, token: JwtAuthenticationToken): Result<DeletarPetHandler> {
            // You can perform any necessary initialization or validation here
            // For simplicity, let's assume no specific initialization is needed

            if (petId <= 0) return Result.failure(
                deletarPetProblem(
                    "Campo 'Id' do pet não pode ser menor ou igual a zero",
                    "usuarioId",
                    petId.toString()
                )
            )

            return Result.success(DeletarPetHandler(petId, token))
        }
    }
}

private fun deletarPetProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel deletar o pet",
    detail = detalhe,
    type = URI("/deletar"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)
