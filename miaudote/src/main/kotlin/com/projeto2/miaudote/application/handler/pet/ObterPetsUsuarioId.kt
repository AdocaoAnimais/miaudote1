package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.pet.PetService
import com.projeto2.miaudote.application.services.adocao.SolicitacaoAdocaoService
import com.projeto2.miaudote.application.services.usuario.UsuarioService
import com.projeto2.miaudote.apresentation.response.pet.PetPost
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.domain.entities.usuario.toProblem
import com.projeto2.miaudote.domain.enums.StatusResponsavel
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI
/**
 * Classe responsável por processar a requisição de obtenção de pets associados a um usuário específico.
 *
 * @property service serviço para gerenciar operações relacionadas a pets.
 * @property solicitacaoService serviço para gerenciar solicitações de adoção.
 * @property usuarioService serviço para gerenciar operações relacionadas a usuários.
 */
@Component
class ObterPetsUsuarioIdProcessor(
    private val service: PetService,
    private val solicitacaoService: SolicitacaoAdocaoService,
    private val usuarioService: UsuarioService,
) : ProcessorHandler<ObterPetsUsuarioIdHandler>() {
    /**
     * Processa a requisição para obter pets associados ao usuário.
     *
     * @param handler objeto contendo os dados necessários para processar a requisição.
     * @return um resultado contendo a lista de pets associados ao usuário ou um erro em caso de falha.
     */
    override fun process(handler: ObterPetsUsuarioIdHandler): Result<Any> {
        usuarioService.obterPorId(handler.id).toProblem().getOrElse { return Result.failure(it) }
        val response = obterPetsUsuario(handler.id)
        return Result.success(response) as Result<Any>
    }
    /**
     * Obtém a lista de pets cadastrados pelo usuário com o ID especificado.
     *
     * @param id o ID do usuário.
     * @return uma lista de objetos do tipo `PetPost`, representando os pets associados ao usuário.
     */
    private fun obterPetsUsuario(id: Long): List<PetPost>? {
        val pets = service.obterPetsUsuario(id)

        val response: List<PetPost>? = pets?.map {
            val solicitacaoAdocao = obterSolicitacaoAdocao(it.id!!).size
            val status = if (solicitacaoAdocao > 0) StatusResponsavel.gerarStatus(solicitacaoAdocao) else null
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
     * Obtém as solicitações de adoção para um pet específico.
     *
     * @param petId o ID do pet.
     * @return uma lista de objetos do tipo `SolicitacaoAdocao` associados ao pet.
     */
    private fun obterSolicitacaoAdocao(petId: Long): List<SolicitacaoAdocao> {
        val solicitacao = solicitacaoService.obterTodasPetId(petId)
        return solicitacao
    }
}
/**
 * Classe que representa o handler para processar a obtenção de pets de um usuário.
 *
 * @property id o ID do usuário solicitante.
 */
class ObterPetsUsuarioIdHandler private constructor(
    val id: Long
) : RequestHandler {
    /**
     * Cria um handler ou retorna um problema caso o ID do usuário seja inválido.
     *
     * @param token o token JWT contendo o ID do usuário.
     * @return um `Result` contendo o handler ou um problema.
     */
    companion object {
        fun newOrProblem(token: JwtAuthenticationToken): Result<ObterPetsUsuarioIdHandler> {
            val id = token.name?.toLongOrNull() ?: return Result.failure(
                obterPorIdProblem("Id do usuário inválido.")
            )

            return Result.success(ObterPetsUsuarioIdHandler(id))
        }
    }
}

/**
 * Cria um objeto de problema para casos onde a operação de obtenção falha.
 *
 * @param detail a mensagem detalhada do problema.
 * @return um objeto `Problem` contendo as informações do erro.
 */
private fun obterPorIdProblem(detail: String) = Problem(
    title = "Não foi possivel obter o pet",
    detail = detail,
    type = URI("/obter-pet-por-id"),
    status = HttpStatus.BAD_REQUEST,
    extra = null
)