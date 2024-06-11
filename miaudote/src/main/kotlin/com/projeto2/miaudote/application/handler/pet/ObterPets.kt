package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.services.SolicitacaoAdocaoService
import com.projeto2.miaudote.apresentation.Response.PetPost
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.domain.enums.getStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
class ObterPetsProcessor(
    private val service: PetService,
    private val solicitacaoService: SolicitacaoAdocaoService
) : ProcessorHandler<ObterPetsHandler>() {
    override fun process(handler: ObterPetsHandler): Result<Any> {
        val response = when (handler.id) {
            is Long -> obterPetsOutrosUsuarios(handler.id)
            else -> service.obterTodosDiponiveis()
        }
        return Result.success(response) as Result<Any>
    }

    private fun obterPetsOutrosUsuarios(id: Long): List<PetPost>? {
        val pets = service.obterPetsOutrosUsuarios(id)

        val response: List<PetPost>? = pets?.map {
            val status = obterSolicitacaoAdocao(id, it.id!!)?.let { solicitacao ->
                it.getStatus(solicitacao)
            }

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

    private fun obterSolicitacaoAdocao(id: Long, petId: Long): SolicitacaoAdocao? {
        val solicitacao = solicitacaoService.obterPorAdotanteIdPetId(usuarioId = id, petId)
        return solicitacao
    }
}

class ObterPetsHandler private constructor(
    val id: Long?
) : RequestHandler {
    companion object {
        fun newOrProblem(token: JwtAuthenticationToken?): Result<ObterPetsHandler> {
            val id = token?.name?.toLongOrNull()

            return Result.success(ObterPetsHandler(id))
        }
    }
}