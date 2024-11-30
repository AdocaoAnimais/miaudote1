package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI

/**
 * Processor responsável por deletar um usuário.
 *
 * Este processador valida a existência do usuário a ser deletado, verifica se ele possui pets associados e, caso necessário,
 * deleta todos os pets antes de deletar o usuário. Retorna o ID do usuário deletado em caso de sucesso.
 *
 * @param service Serviço responsável por gerenciar os usuários.
 * @param petService Serviço responsável por gerenciar os pets dos usuários.
 */
@Component
class DeletarUsuarioProcessor(
    private val service: UsuarioService,
    private val petService: PetService,
) : ProcessorHandler<DeletarUsuarioHandler>() {
    /**
     * Processa a solicitação de exclusão de um usuário.
     *
     * Este método verifica se o usuário existe e se ele possui pets associados, deletando os pets antes de deletar o usuário.
     *
     * @param handler Dados necessários para deletar o usuário.
     * @return Resultado da operação de deleção, incluindo o ID do usuário deletado.
     */
    override fun process(handler: DeletarUsuarioHandler): Result<Any> {
        val id = handler.id
        // testar se o usuário de fato existe
        val pets = petService.obterPetsUsuario(id) ?: emptyList()
        service.obterPorId(id).toProblem().getOrElse { return Result.failure(it) }
        service.deletar(id)
        petService.deletarTodos(pets)
        return Result.success(id)
    }
}

/**
 * Classe de handler que contém o ID do usuário a ser deletado.
 *
 * Este handler contém os dados necessários para identificar o usuário que será deletado.
 *
 * @param id ID do usuário a ser deletado.
 */
class DeletarUsuarioHandler private constructor(
    val id: Long
) : RequestHandler {
    /**
     * Cria um novo handler para a deleção de usuário ou retorna um erro caso o ID não seja válido.
     *
     * Este método valida o ID do usuário para garantir que a deleção seja realizada corretamente.
     *
     * @param token Token de autenticação contendo o ID do usuário a ser deletado.
     * @return Resultado contendo o handler de deleção ou erro de validação.
     */
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

/**
 * Cria um problema com detalhes sobre a falha ao tentar deletar um usuário.
 *
 * Este método cria um problema que será retornado quando ocorrer algum erro ao tentar deletar um usuário,
 * como a inexistência do ID do usuário.
 *
 * @param detalhe Descrição detalhada do erro ocorrido.
 * @param campo Campo que causou o erro.
 * @param valor Valor do campo que causou o erro.
 * @return O problema gerado com os detalhes do erro.
 */
private fun deletarProblemaUsuario(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel deletar o usuario",
    detail = detalhe,
    type = URI("/deletar-usuario"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)
