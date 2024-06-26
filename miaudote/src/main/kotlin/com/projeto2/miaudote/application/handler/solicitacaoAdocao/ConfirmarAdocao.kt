package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.*
import com.projeto2.miaudote.domain.entities.Adocao
import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@Component
class ConfirmarAdocaoProcessor(
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val adocaoService: AdocaoService,
    val emailService: EmailService,
) : ProcessorHandler<ConfirmarAdocaoHandler>() {
    override fun process(handler: ConfirmarAdocaoHandler): Result<Any> {
        val solicitacao = service.obterPorId(handler.solicitacaoId).toProblem().getOrElse {
            return Result.failure(it)
        }
        val pet = petService.obterPorId(solicitacao.petId).toProblem().getOrElse {
            return Result.failure(it)
        }
        if (adocaoService.obterPorPetId(pet.id!!) != null) return Result.failure(
            adocaoInvalida("O pet ${pet.nome} já foi adotado.", null)
        )

        val adotante = usuarioService.obterPorId(solicitacao.usuarioAdotante).toProblem().getOrElse {
            return Result.failure(it)
        }
        val responsavel = usuarioService.obterPorId(solicitacao.usuarioResponsavel).toProblem().getOrElse {
            return Result.failure(it)
        }
        val solicitacaoAtualizada = solicitacao.copy(
            dataConfirmacaoUserAdotante = LocalDateTime.now()
        )
        service.atualizar(solicitacaoAtualizada)

        val adocao = Adocao(
            id = null,
            petId = pet.id,
            dataAdocao = LocalDateTime.now(),
            solicitacaoId = solicitacao.id!!
        )

        adocaoService.criar(adocao)

        notificarResponsavel(responsavel = responsavel, pet = pet)

        notificarAdotante(adotante = adotante, pet = pet)

        return Result.success("Sucesso!!")
    }

    private fun notificarResponsavel(responsavel: Usuario, pet: Pet) {
        val conteudo = """
            Recebemos a confirmação do novo tutor de ${pet.nome} que a adoção foi concluída com sucesso.
            Então ${pet.nome} não esta mais dísponivel para adoção!!
        """.trimIndent()
        emailService.enviarEmail(
            to = responsavel.email,
            subject = "[MIAUDOTE] Adoção Concluída com SUCESSO! - ${pet.nome}",
            conteudo = conteudo
        )
    }

    private fun notificarAdotante(adotante: Usuario, pet: Pet) {
        val conteudo = """
            Recebemos a confirmação da adoção de ${pet.nome} foi concluída com sucesso.
            Então ${pet.nome} não esta mais dísponivel para adoção!!
        """.trimIndent()
        emailService.enviarEmail(
            to = adotante.email,
            subject = "[MIAUDOTE] Adoção Concluída com SUCESSO! - ${pet.nome}",
            conteudo = conteudo
        )
    }
}

class ConfirmarAdocaoHandler private constructor(
    val solicitacaoId: UUID,
) : RequestHandler {
    companion object {
        fun newOrProblem(solicitacaoIdIn: String): Result<ConfirmarAdocaoHandler> {
            val solicitacaoId = UUID.fromString(solicitacaoIdIn) ?: return Result.failure(
                adocaoInvalida("Id da solicitação de adoção inválida.", null)
            )
            return Result.success(
                ConfirmarAdocaoHandler(
                    solicitacaoId = solicitacaoId,
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