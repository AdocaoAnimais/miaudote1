package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.adocao.AdocaoService
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.apresentation.Request.PetCreate
import com.projeto2.miaudote.domain.enums.*
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI
/**
 * Processa a atualização de informações de um pet.
 *
 * @property petService serviço responsável pelas operações de pets.
 * @property adocaoService serviço responsável pelas operações de adoção.
 */
@Component
class AtualizarPetProcessor(
    private val petService: PetService,
    private val adocaoService: AdocaoService,
) : ProcessorHandler<AtualizarPetHandler>() {
    /**
     * Processa a atualização de um pet, validando permissões e verificando se o pet pode ser atualizado.
     *
     * @param handler contém os dados necessários para a atualização.
     * @return resultado contendo o pet atualizado ou informações de erro.
     */
    override fun process(handler: AtualizarPetHandler): Result<Any> {
        val id = handler.token.name.toLongOrNull() ?: return atualizarPetProblem(
            "Id do usuário não encontrado.",
            "usuarioId",
        ).toFailure()

        val petExistente = petService.obterPorId(handler.id) ?: return atualizarPetProblem(
            "Pet não encontrado",
            "pet",
        ).toFailure()

        if(adocaoService.obterPorPetId(handler.id) != null) return atualizarPetProblem(
            "Pet que já foi adotado não pode ser editado.",
            "pet",
        ).toFailure()

        if (petExistente.idUsuario != id) {
            return atualizarPetProblem(
                "Usuário não tem permissão para atualizar este pet",
                "usuarioId",
                id.toString()
            ).toFailure()
        }

        val petAtualizado = petExistente.copy(
            nome = handler.nome ?: petExistente.nome,
            idade = handler.idade ?: petExistente.idade,
            porte = handler.porte ?: petExistente.porte,
            sexo = handler.sexo ?: petExistente.sexo,
            tipo = handler.tipo ?: petExistente.tipo,
            castrado = handler.castrado ?: petExistente.castrado,
            descricao = handler.descricao ?: petExistente.descricao,
            idUsuario = id,
        )

        val result = petService.atualizar(petAtualizado)
        return Result.success(result)
    }
}
/**
 * Manipulador para dados de entrada na atualização de um pet.
 *
 * @property id ID do pet a ser atualizado.
 * @property nome nome atualizado do pet.
 * @property sexo sexo atualizado do pet.
 * @property porte porte atualizado do pet.
 * @property idade idade atualizada do pet.
 * @property tipo tipo atualizado do pet.
 * @property castrado status de castração atualizado do pet.
 * @property descricao descrição atualizada do pet.
 * @property token token de autenticação do usuário.
 */
class AtualizarPetHandler private constructor(
    val id: Long,
    val nome: String?,
    val sexo: Sexo?,
    val porte: Porte?,
    val idade: Int?,
    val tipo: Tipo?,
    val castrado: Castrado?,
    val descricao: String?,
    val token: JwtAuthenticationToken
) : RequestHandler {
    companion object {
        /**
         * Cria uma instância de AtualizarPetHandler ou retorna um problema em caso de validações inválidas.
         *
         * @param petId ID do pet.
         * @param petIn dados do pet a serem atualizados.
         * @param token token de autenticação.
         * @return instância de AtualizarPetHandler ou problema de validação.
         */
        fun newOrProblem(
            petId: Long?,
            petIn: PetCreate,
            token: JwtAuthenticationToken
        ): Result<AtualizarPetHandler> {
            val id = token.name.toLongOrNull() ?: return Result.failure(
                atualizarPetProblem(
                    "Id do usuário não encontrado.",
                    "usuarioId",
                )
            )
            if (id <= 0) return Result.failure(
                atualizarPetProblem(
                    "Campo 'usuarioId' não pode ser menor ou igual a zero",
                    "usuarioId",
                    id.toString()
                )
            )
            val idIn = petId ?: return Result.failure(
                atualizarPetProblem(
                    "Campo 'id' não pode ser null",
                    "id",
                    petId.toString()
                )
            )
            val nomeIn = petIn.validaNome().getOrElse { return Result.failure(it) }
            val sexoIn = petIn.validaSexo().getOrElse { return Result.failure(it) }
            val porteIn = petIn.validaPorte().getOrElse { return Result.failure(it) }
            val idadeIn = petIn.validaIdade().getOrElse { return Result.failure(it) }
            val tipoIn = petIn.validaTipo().getOrElse { return Result.failure(it) }
            val castradoIn = petIn.validaCastrado().getOrElse { return Result.failure(it) }
            val descricaoIn = petIn.validaDescricao().getOrElse { return Result.failure(it) }

            val response = AtualizarPetHandler(
                id = idIn,
                nome = nomeIn,
                sexo = sexoIn,
                porte = porteIn,
                idade = idadeIn,
                tipo = tipoIn,
                castrado = castradoIn,
                descricao = descricaoIn,
                token = token
            )
            return Result.success(response)
        }
    }
}
/**
 * Cria um objeto de problema específico para erros na atualização de um pet.
 *
 * @param detalhe mensagem detalhada do problema.
 * @param campo campo que causou o problema.
 * @param valor valor fornecido que causou o erro, default é "null".
 * @return objeto de problema configurado.
 */
private fun atualizarPetProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possível atualizar o pet",
    detail = detalhe,
    type = URI("/atualizar"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)