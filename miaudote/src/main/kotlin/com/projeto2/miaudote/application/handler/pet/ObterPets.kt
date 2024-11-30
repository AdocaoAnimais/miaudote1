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
/**
 * Processa a obtenção de pets disponíveis para adoção.
 *
 * @property service serviço responsável pelas operações relacionadas a pets.
 * @property solicitacaoService serviço responsável pelas solicitações de adoção.
 */
@Component
class ObterPetsProcessor(
    private val service: PetService,
    private val solicitacaoService: SolicitacaoAdocaoService
) : ProcessorHandler<ObterPetsHandler>() {
    /**
     * Obtém a lista de pets disponíveis, podendo incluir pets de outros usuários.
     *
     * @param handler contém os dados necessários para o processamento.
     * @return resultado contendo a lista de pets ou informações de erro.
     */
    override fun process(handler: ObterPetsHandler): Result<Any> {
        val response = when (handler.id) {
            is Long -> obterPetsOutrosUsuarios(handler.id)
            else -> service.obterTodosDiponiveis()
        }
        return Result.success(response) as Result<Any>
    }
    /**
     * Obtém a lista de pets cadastrados por outros usuários.
     *
     * @param id ID do usuário atual.
     * @return lista de pets mapeados para o formato de resposta.
     */
    private fun obterPetsOutrosUsuarios(id: Long): List<PetPost>? {
        val pets = service.obterPetsOutrosUsuarios(id)

        val response: List<PetPost>? = pets?.map {
            val status = obterSolicitacaoAdocao(id, it.id!!)?.let { solicitacao ->
                it.getStatus(solicitacao)
            }

            PetPost(
                id = it.id,
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
    /**
     * Verifica se existe uma solicitação de adoção entre o usuário e o pet informado.
     *
     * @param id ID do usuário.
     * @param petId ID do pet.
     * @return solicitação de adoção, se existente.
     */
    private fun obterSolicitacaoAdocao(id: Long, petId: Long): SolicitacaoAdocao? {
        val solicitacao = solicitacaoService.obterPorAdotanteIdPetId(usuarioId = id, petId)
        return solicitacao
    }
}

/**
 * Manipulador para dados de entrada na obtenção de pets.
 *
 * @property id ID do usuário solicitante, se disponível.
 */
class ObterPetsHandler private constructor(
    val id: Long?
) : RequestHandler {
    companion object {
        /**
         * Valida e cria uma instância de ObterPetsHandler.
         *
         * @param token token de autenticação do usuário.
         * @return instância de ObterPetsHandler ou problema de validação.
         */
        fun newOrProblem(token: JwtAuthenticationToken?): Result<ObterPetsHandler> {
            val id = token?.name?.toLongOrNull()

            return Result.success(ObterPetsHandler(id))
        }
    }
}