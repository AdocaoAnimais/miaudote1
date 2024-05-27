package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.*
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.URI
import java.util.*

@Service
class CancelarAdocaoProcessor(
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val emailService: EmailService,
    val adocaoService: AdocaoService,
) : ProcessorHandler<CancelarAdocaoHandler>() {
    override fun process(handler: CancelarAdocaoHandler): Result<Any> {
        val solicitacao = service.obterPorId(handler.solicitacaoAdocaoId).toProblem().getOrElse {
            return Result.failure(it)
        }
        val pet = petService.obterPorId(solicitacao.petId).toProblem().getOrElse {
            return Result.failure(it)
        }
        val adotante = usuarioService.obterPorId(solicitacao.usuarioAdotante).toProblem().getOrElse {
            return Result.failure(it)
        }
        val responsavel = usuarioService.obterPorId(solicitacao.usuarioResponsavel).toProblem().getOrElse {
            return Result.failure(it)
        }

        service.deletar(solicitacao.id!!)

        emailService.enviarEmail(
            to = responsavel.email,
            subject = "[MIAUDOTE] Adoção Cancelada - ${pet.nome}",
            conteudo = geraConteudo(pet.nome)
        )
        emailService.enviarEmail(
            to = adotante.email,
            subject = "[MIAUDOTE] Adoção Cancelada com Sucesso- ${pet.nome}",
            conteudo = geraConteudo(pet.nome)
        )

        return Result.success("Adoção cancelada com sucesso!!")
    }

    fun geraConteudo(nomePet: String): String {
        val conteudo = """
            A adoção do animal $nomePet foi cancelada pelo adotante, 
            o animal continua disponível para adoção.
            
            Não responda este email.
        """.trimIndent()
        return conteudo
    }
}

class CancelarAdocaoHandler private constructor(
    val solicitacaoAdocaoId: UUID,
) : RequestHandler {
    companion object {
        fun newOrProblem(solicitacaoAdocaoIdIn: String): Result<CancelarAdocaoHandler> {
            val solicitacaoAdocaoId = UUID.fromString(solicitacaoAdocaoIdIn) ?: return Result.failure(
                adocaoInvalida("Id da solicitação da adoção inválida.", null)
            )
            return Result.success(
                CancelarAdocaoHandler(
                    solicitacaoAdocaoId = solicitacaoAdocaoId,
                )
            )
        }
    }
}

private fun adocaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel cancelar a adoção.",
    detail = detail,
    type = URI("/cancelar-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)