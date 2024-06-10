package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.apresentation.Request.PetCreate
import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.domain.enums.*
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI
import java.time.LocalDateTime

@Component
class CriarPetProcessor(
    val service: PetService,
    val usuarioService: UsuarioService,
) : ProcessorHandler<CriarPetHandler>() {
    override fun process(handler: CriarPetHandler): Result<Any> {

        val usuario = usuarioService.obterPorId(handler.idUsuario) ?: return criarPetProblem(
            "Usuario invalido para cadastro de pet",
            "usuario",
        ).toFailure()
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
        fun newOrProblem(petIn: PetCreate, token: JwtAuthenticationToken): Result<CriarPetHandler> {
            val id = token.name.toLongOrNull() ?: return Result.failure(
                criarPetProblem(
                    "Id do usuário inválido.",
                    "ID",
                )
            )
            val nomeIn = petIn.nome
            if (nomeIn.isNullOrBlank() || nomeIn.length <= 2) return Result.failure(
                criarPetProblem(
                    "Campo 'nome' não pode ser null ou menor que três caracteres",
                    "nome",
                    petIn.nome
                )
            )

            val sexoIn = petIn.sexo?.toSexo()?.getOrElse {
                return Result.failure(it)
            } ?: return Result.failure(
                criarPetProblem(
                    "Campo 'sexo' não pode ser null",
                    "sexo",
                    petIn.sexo
                )
            )

            val porteIn = petIn.porte?.toPorte()?.getOrElse {
                return Result.failure(it)
            } ?: return Result.failure(
                criarPetProblem(
                    "Campo 'porte' não pode ser null",
                    "porte",
                    petIn.porte
                )
            )

            val idadeIn = petIn.idade?.toIntOrNull() ?: 0
            if(idadeIn > 40)  return Result.failure(
                criarPetProblem(
                    "Nenhum cão ou gato vive tanto tempo.",
                    "idade",
                    petIn.tipo
                )
            )

            val tipoIn = petIn.tipo?.toTipo()?.getOrElse {
                return Result.failure(it)
            } ?: return Result.failure(
                criarPetProblem(
                    "Campo 'tipo' não pode ser null",
                    "tipo",
                    petIn.tipo
                )
            )

            val castradoIn = petIn.castrado?.toCastrado()?.getOrElse {
                return Result.failure(it)
            } ?: return Result.failure(
                criarPetProblem(
                    "Campo 'castrado' não pode ser null",
                    "castrado",
                    petIn.castrado
                )
            )

            val response = CriarPetHandler(
                nome = nomeIn,
                sexo = sexoIn,
                porte = porteIn,
                idade = idadeIn,
                tipo = tipoIn,
                castrado = castradoIn,
                descricao = petIn.descricao,
                idUsuario = id
            )
            return Result.success(response)
        }
    }
}

private fun criarPetProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel criar o pet",
    detail = detalhe,
    type = URI("/cadastrar-pet"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)
