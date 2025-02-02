package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.pet.PetService
import com.projeto2.miaudote.application.services.usuario.UsuarioService
import com.projeto2.miaudote.apresentation.request.pet.PetCreate
import com.projeto2.miaudote.domain.entities.pet.Pet
import com.projeto2.miaudote.domain.enums.*
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI
import java.time.LocalDateTime
/**
 * Processa a criação de um novo pet no sistema.
 *
 * @property service serviço responsável pelas operações relacionadas a pets.
 * @property usuarioService serviço responsável pelas operações relacionadas a usuários.
 */
@Component
class CriarPetProcessor(
    val service: PetService,
    val usuarioService: UsuarioService,
) : ProcessorHandler<CriarPetHandler>() {
    /**
     * Processa a criação de um pet, validando o usuário e seus requisitos antes de cadastrar o pet.
     *
     * @param handler contém os dados necessários para a criação do pet.
     * @return resultado contendo o pet criado ou informações de erro.
     */
    override fun process(handler: CriarPetHandler): Result<Any> {

        val usuario = usuarioService.obterPorId(handler.idUsuario) ?: return criarPetProblem(
            "Usuario invalido para cadastro de pet",
            "usuario",
        ).toFailure()
        if (!usuario.emailVerificado) return Result.failure(
            criarPetProblem(
                "Email de cadastro não verificado, verifique seu email antes de cadastrar um animal!",
                ""
            )
        )
        val pet = Pet(
            nome = handler.nome,
            idade = handler.idade,
            porte = handler.porte,
            sexo = handler.sexo,
            tipo = handler.tipo,
            castrado = handler.castrado,
            descricao = handler.descricao,
            idUsuario = handler.idUsuario,
            dataCadastro = LocalDateTime.now(),
            id = null,
            imageData = null,
        )
        val result = service.criar(pet)
        return Result.success(result)
    }
}
/**
 * Manipulador para dados de entrada na criação de um pet.
 *
 * @property nome nome do pet.
 * @property sexo sexo do pet.
 * @property porte porte do pet.
 * @property idade idade do pet.
 * @property tipo tipo do pet.
 * @property castrado status de castração do pet.
 * @property descricao descrição adicional do pet.
 * @property idUsuario ID do usuário associado ao pet.
 */
class CriarPetHandler private constructor(
    val nome: String,
    val sexo: Sexo,
    val porte: Porte,
    val idade: Int,
    val tipo: Tipo,
    val castrado: Castrado,
    val descricao: String?,
    val idUsuario: Long
) : RequestHandler {
    companion object {
        /**
         * Valida e cria uma instância de CriarPetHandler, retornando problemas em caso de dados inválidos.
         *
         * @param petIn dados do pet a serem validados.
         * @param token token de autenticação do usuário.
         * @return instância de CriarPetHandler ou problema de validação.
         */
        fun newOrProblem(petIn: PetCreate, token: JwtAuthenticationToken): Result<CriarPetHandler> {
            val id = token.name.toLongOrNull() ?: return Result.failure(
                criarPetProblem(
                    "Id do usuário inválido.",
                    "ID",
                )
            )
            val nomeIn = petIn.validaNome().getOrElse { return Result.failure(it) }
            val sexoIn = petIn.validaSexo().getOrElse { return Result.failure(it) }
            val porteIn = petIn.validaPorte().getOrElse { return Result.failure(it) }
            val idadeIn = petIn.validaIdade().getOrElse { return Result.failure(it) }
            val tipoIn = petIn.validaTipo().getOrElse { return Result.failure(it) }
            val castradoIn = petIn.validaCastrado().getOrElse { return Result.failure(it) }
            val descricaoIn = petIn.validaDescricao().getOrElse { return Result.failure(it) }

            val response = CriarPetHandler(
                nome = nomeIn,
                sexo = sexoIn,
                porte = porteIn,
                idade = idadeIn,
                tipo = tipoIn,
                castrado = castradoIn,
                descricao = descricaoIn,
                idUsuario = id
            )
            return Result.success(response)
        }
    }
}
/**
 * Cria um objeto de problema específico para erros na criação de um pet.
 *
 * @param detalhe mensagem detalhada do problema.
 * @param campo campo que causou o problema.
 * @return objeto de problema configurado.
 */
private fun criarPetProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel criar o pet",
    detail = detalhe,
    type = URI("/cadastrar-pet"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)
