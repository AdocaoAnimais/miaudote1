package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.pet.PetService
import com.projeto2.miaudote.domain.entities.pet.toProblem
import com.projeto2.miaudote.domain.entities.usuario.toProblem
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI
/**
 * Processa a exclusão de um pet no sistema.
 *
 * @property service serviço responsável pelas operações relacionadas a pets.
 */
@Component
class DeletarPetProcessor(
    private val service: PetService,
) : ProcessorHandler<DeletarPetHandler>() {
    /**
     * Processa a exclusão de um pet, verificando permissões e existência do pet.
     *
     * @param handler contém os dados necessários para a exclusão.
     * @return resultado contendo o ID do pet excluído ou informações de erro.
     */
    override fun process(handler: DeletarPetHandler): Result<Any> {
        val pet = service.obterPorId(handler.petId) ?: return deletarPetProblem(
            "Pet não encontrado",
            "pet",
        ).toFailure()

        if (pet.idUsuario != handler.usuarioId) {
            return deletarPetProblem(
                "Usuário não tem permissão para atualizar este pet",
                "usuarioId",
                handler.usuarioId.toString()
            ).toFailure()
        }
        // testar se o pet de fato existe
        service.obterPorId(handler.petId).toProblem().getOrElse { return Result.failure(it) }
        service.deletar(handler.petId)
        return Result.success(handler.petId)
    }
}
/**
 * Manipulador para dados de entrada na exclusão de um pet.
 *
 * @property petId ID do pet a ser excluído.
 * @property usuarioId ID do usuário solicitante.
 */
class DeletarPetHandler(
    val petId: Long,
    val usuarioId: Long
) : RequestHandler {
    companion object {
        /**
         * Valida e cria uma instância de DeletarPetHandler, retornando problemas em caso de dados inválidos.
         *
         * @param petId ID do pet.
         * @param token token de autenticação do usuário.
         * @return instância de DeletarPetHandler ou problema de validação.
         */
        fun newOrProblem(petId: Long, token: JwtAuthenticationToken): Result<DeletarPetHandler> {
            // You can perform any necessary initialization or validation here
            // For simplicity, let's assume no specific initialization is needed
            val usuarioId = token.name.toLongOrNull() ?: return Result.failure(
                deletarPetProblem(
                    "Id do usuário não encontrado.",
                    "ID",
                )
            )

            if (petId <= 0) return Result.failure(
                deletarPetProblem(
                    "Campo 'Id' do pet não pode ser menor ou igual a zero",
                    "usuarioId",
                    petId.toString()
                )
            )

            return Result.success(DeletarPetHandler(petId, usuarioId))
        }
    }
}
/**
 * Cria um objeto de problema específico para erros na exclusão de um pet.
 *
 * @param detalhe mensagem detalhada do problema.
 * @param campo campo que causou o problema.
 * @param valor valor fornecido que causou o erro, default é "null".
 * @return objeto de problema configurado.
 */
private fun deletarPetProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel deletar o pet",
    detail = detalhe,
    type = URI("/deletar"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)
