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
/**
 * Processa a obtenção de um pet por ID.
 *
 * @property service serviço responsável pelas operações relacionadas a pets.
 */
@Component
class ObterPetPorIdProcessor(
    private val service: PetService,
) : ProcessorHandler<ObterPetPorIdHandler>() {
    /**
     * Obtém um pet por ID, validando a associação com o usuário.
     *
     * @param handler contém os dados necessários para a busca.
     * @return resultado contendo o pet ou informações de erro.
     */
    override fun process(handler: ObterPetPorIdHandler): Result<Any> {
        val pet = service.obterPorId(handler.id).toProblem().getOrElse { return Result.failure(it) }
        if(pet.idUsuario != handler.idUsuario) return Result.failure(
            obterPorIdProblem("Usuário não cadastrou o pet.")
        )
        return Result.success(pet)
    }
}

/**
 * Manipulador para dados de entrada na obtenção de um pet por ID.
 *
 * @property id ID do pet a ser buscado.
 * @property idUsuario ID do usuário solicitante.
 */
class ObterPetPorIdHandler private constructor(
    val id: Long,
    val idUsuario: Long,
) : RequestHandler {
    companion object {
        /**
         * Valida e cria uma instância de ObterPetPorIdHandler, retornando problemas em caso de dados inválidos.
         *
         * @param idPet ID do pet.
         * @param token token de autenticação do usuário.
         * @return instância de ObterPetPorIdHandler ou problema de validação.
         */
        fun newOrProblem(idPet: Long, token: JwtAuthenticationToken): Result<ObterPetPorIdHandler> {
            val idUsuario = token.name.toLongOrNull() ?: return Result.failure(
                obterPorIdProblem("Id do usuário inválido.")
            )

            return Result.success(ObterPetPorIdHandler(idPet, idUsuario))
        }
    }
}
/**
 * Cria um objeto de problema específico para erros na obtenção de um pet.
 *
 * @param detail mensagem detalhada do problema.
 * @return objeto de problema configurado.
 */
private fun obterPorIdProblem(detail: String) = Problem(
    title = "Não foi possivel obter o pet",
    detail = detail,
    type = URI("/obter-pet-por-id"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)