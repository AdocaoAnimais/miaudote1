package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.*
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@Service
class ConfirmarSolicitacaoProcessor(
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val emailService: EmailService,
    val adocaoService: AdocaoService,
) : ProcessorHandler<ConfirmarSolicitacaoHandler>() {
    override fun process(handler: ConfirmarSolicitacaoHandler): Result<Any> {
        val pet = petService.obterPorId(handler.petId).toProblem().getOrElse {
            return Result.failure(it)
        }
        if (adocaoService.obterPorPetId(pet.id!!) != null) return Result.failure(
            solicitacaoInvalida("O pet ${pet.nome} já foi adotado.", null)
        )

        val responsavel = usuarioService.obterUsername(handler.username).toProblem().getOrElse {
            return Result.failure(it)
        }

        val solicitacaoAdocao = service.obterPorId(UUID.randomUUID()).toProblem().getOrElse {
            return Result.failure(it)
        }

        val adotante = usuarioService.obterPorId(solicitacaoAdocao.usuarioAdotante).toProblem().getOrElse {
            return Result.failure(it)
        }

        val solicitacaoAtualizada = solicitacaoAdocao.update(
            dataConfirmacaoUserResponsavel = LocalDateTime.now(),
            dataConfirmacaoUserAdotante = null
        )

        emailService.enviarEmailUsuarioSolicitante(
            adotante = adotante,
            pet = pet,
            linkConfirmaAdocao = "http://localhost:8080/solicitacao-adocao/confirmar-adocao/${responsavel.username}/${pet.id}",
            linkCancelaAdocao = "http://localhost:8080/solicitacao-adocao/cancelar-adocao/${responsavel.username}/${pet.id}",
            responsavel = responsavel
        )

        service.atualizar(solicitacaoAtualizada)

        return Result.success("Sucesso!!")
    }
}

class ConfirmarSolicitacaoHandler private constructor(
    val username: String,
    val petId: Long,
) : RequestHandler {
    companion object {
        fun newOrProblem(petIdIn: String, username: String): Result<ConfirmarSolicitacaoHandler> {
            val petId = petIdIn.toLongOrNull() ?: return Result.failure(
                solicitacaoInvalida("Id do pet inválido.", null)
            )
            return Result.success(
                ConfirmarSolicitacaoHandler(
                    username = username,
                    petId = petId
                )
            )
        }
    }
}

private fun solicitacaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel confirmar a solicitação de adoção.",
    detail = detail,
    type = URI("/confirmar-solicitacao-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)