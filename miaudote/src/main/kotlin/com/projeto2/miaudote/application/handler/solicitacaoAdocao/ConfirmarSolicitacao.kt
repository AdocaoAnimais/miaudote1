package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.*
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${base.url}")
    private val baseUrl: String
) : ProcessorHandler<ConfirmarSolicitacaoHandler>() {
    override fun process(handler: ConfirmarSolicitacaoHandler): Result<Any> {
        val solicitacaoAdocao = service.obterPorId(handler.solicitacaoId).toProblem().getOrElse {
            return Result.failure(it)
        }
        val pet = petService.obterPorId(solicitacaoAdocao.petId).toProblem().getOrElse {
            return Result.failure(it)
        }
        if (adocaoService.obterPorPetId(pet.id!!) != null) return Result.failure(
            solicitacaoInvalida("O pet ${pet.nome} já foi adotado.", null)
        )
        val responsavel = usuarioService.obterPorId(solicitacaoAdocao.usuarioResponsavel).toProblem().getOrElse {
            return Result.failure(it)
        }
        val adotante = usuarioService.obterPorId(solicitacaoAdocao.usuarioAdotante).toProblem().getOrElse {
            return Result.failure(it)
        }
        val solicitacaoAtualizada = solicitacaoAdocao.update(
            dataConfirmacaoUserResponsavel = LocalDateTime.now(),
            dataConfirmacaoUserAdotante = null
        )

        emailService.enviarEmailUsuarioAdotante(
            adotante = adotante,
            pet = pet,
            linkConfirmaAdocao = "$baseUrl/solicitacao-adocao/confirmar-adocao/${solicitacaoAdocao.id}",
            linkCancelaAdocao = "$baseUrl/solicitacao-adocao/cancelar-adocao/${solicitacaoAdocao.id}",
            responsavel = responsavel
        )
        service.atualizar(solicitacaoAtualizada)
        return Result.success("Sucesso!!")
    }
}

class ConfirmarSolicitacaoHandler private constructor(
    val solicitacaoId: UUID,
) : RequestHandler {
    companion object {
        fun newOrProblem(solicitacaoIdIn: String): Result<ConfirmarSolicitacaoHandler> {
            val solicitacaoId = UUID.fromString(solicitacaoIdIn) ?: return Result.failure(
                solicitacaoInvalida("Id da solicitação da adoção inválido.", null)
            )
            return Result.success(
                ConfirmarSolicitacaoHandler(
                    solicitacaoId = solicitacaoId,
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