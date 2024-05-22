package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.AdocaoService
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.services.SolicitacaoAdocaoService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.domain.entities.Adocao
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.URI
import java.util.*

@Service
class ConfirmarAdocaoProcessor(
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val adocaoService: AdocaoService,
) : ProcessorHandler<ConfirmarAdocaoHandler>() {
    override fun process(handler: ConfirmarAdocaoHandler): Result<Any> {
        val pet = petService.obterPorId(handler.petId).toProblem().getOrElse {
            return Result.failure(it)
        }
        if (adocaoService.obterPorPetId(pet.id!!) != null) return Result.failure(
            adocaoInvalida("O pet ${pet.nome} já foi adotado.", null)
        )

        val adotante = usuarioService.obterUsername(handler.username).toProblem().getOrElse {
            return Result.failure(it)
        }

        val solicitacao = service.obterPorId(UUID.randomUUID()).toProblem().getOrElse {
            return Result.failure(it)
        }
        val responsavel = usuarioService.obterPorId(solicitacao.usuarioResponsavel).toProblem().getOrElse {
            return Result.failure(it)
        }

        val adocao = Adocao()

        adocaoService.criar(adocao)

        return Result.success("")
    }
}

class ConfirmarAdocaoHandler private constructor(
    val username: String,
    val petId: Long,
) : RequestHandler {
    companion object {
        fun newOrProblem(petIdIn: String, username: String): Result<ConfirmarAdocaoHandler> {
            val petId = petIdIn.toLongOrNull() ?: return Result.failure(
                adocaoInvalida("Id do pet inválido.", null)
            )
            return Result.success(
                ConfirmarAdocaoHandler(
                    username = username,
                    petId = petId
                )
            )
        }
    }
}

private fun adocaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel confirmar a adoção.",
    detail = detail,
    type = URI("/confirmar-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)