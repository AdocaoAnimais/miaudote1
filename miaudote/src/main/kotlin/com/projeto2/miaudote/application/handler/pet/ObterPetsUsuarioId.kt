package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.services.SolicitacaoAdocaoService
import com.projeto2.miaudote.apresentation.Response.PetPost
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.domain.enums.StatusResponsavel
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI

@Component
class ObterPetsUsuarioIdProcessor(
    private val service: PetService,
    private val solicitacaoService: SolicitacaoAdocaoService
) : ProcessorHandler<ObterPetsUsuarioIdHandler>() {
    override fun process(handler: ObterPetsUsuarioIdHandler): Result<Any> {
        val response = when (handler.id) {
            is Long -> obterPetsUsuario(handler.id)
            else -> service.obterTodosDiponiveis()
        }
        return Result.success(response) as Result<Any>
    }

    private fun obterPetsUsuario(id: Long): List<PetPost>? {
        val pets = service.obterPetsUsuario(id)

        val response: List<PetPost>? = pets?.map {
            val solicitacaoAdocao = obterSolicitacaoAdocao(id, it.id!!).size
            val status = if(solicitacaoAdocao > 0) StatusResponsavel.gerarStatus(solicitacaoAdocao) else null
            PetPost(
                id = it.id!!,
                nome = it.nome,
                descricao = it.descricao,
                sexo = it.sexo,
                porte = it.porte,
                castrado = it.castrado,
                imageData = it.imageData?.binaryStream?.readBytes(),
                status = status
            )
        }

        return response
    }

    private fun obterSolicitacaoAdocao(id: Long, petId: Long): List<SolicitacaoAdocao> {
        val solicitacao = solicitacaoService.obterTodasPetId(petId)
        return solicitacao
    }
}

class ObterPetsUsuarioIdHandler private constructor(
    val id: Long
) : RequestHandler {
    companion object {
        fun newOrProblem(token: JwtAuthenticationToken): Result<ObterPetsUsuarioIdHandler> {
            val id = token.name?.toLongOrNull() ?: return Result.failure(
                obterPorIdProblem("Id do usuário inválido.")
            )

            return Result.success(ObterPetsUsuarioIdHandler(id))
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