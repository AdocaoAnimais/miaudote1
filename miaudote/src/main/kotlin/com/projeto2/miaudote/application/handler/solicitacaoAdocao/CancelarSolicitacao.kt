package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.EmailService
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.services.SolicitacaoAdocaoService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.URI
import java.util.*

@Service
class CancelarSolicitacaoProcessor(
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val emailService: EmailService,
) : ProcessorHandler<CancelarSolicitacaoHandler>() {
    override fun process(handler: CancelarSolicitacaoHandler): Result<Any> {
        val responsavel = usuarioService.obterUsername(handler.username).toProblem().getOrElse {
            return Result.failure(it)
        }

        val pet = petService.obterPorId(handler.petId).toProblem().getOrElse {
            return Result.failure(it)
        }

        val solicitacaoAdocao = service.obterPorId(UUID.randomUUID()).toProblem().getOrElse {
            return Result.failure(it)
        }

        val adotante = usuarioService.obterPorId(solicitacaoAdocao.usuarioAdotante).toProblem().getOrElse {
            return Result.failure(it)
        }

        service.deletar(solicitacaoAdocao.id!!)

        emailService.enviarEmail(
            to = adotante.email,
            subject = "[MIAUDOTE] Solicitação Cancelada - ${pet.nome}",
            conteudo = geraConteudo(pet.nome)
        )
        return Result.success("Sucesso!!")
    }

    fun geraConteudo(nomePet: String): String {
        val conteudo = """
            A solicitação da adoção do animal $nomePet foi cancelada pelo responsável, 
            o animal continua disponível para adoção.
            
            Não responda este email.
        """.trimIndent()
        return conteudo
    }
}

class CancelarSolicitacaoHandler private constructor(
    val username: String,
    val petId: Long,
) : RequestHandler {
    companion object {
        fun newOrProblem(petIdIn: String, username: String): Result<CancelarSolicitacaoHandler> {
            val petId = petIdIn.toLongOrNull() ?: return Result.failure(
                solicitacaoInvalida("Id do pet inválido.", null)
            )
            return Result.success(
                CancelarSolicitacaoHandler(
                    username = username,
                    petId = petId
                )
            )
        }
    }
}

private fun solicitacaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel cancelar a solicitação de adoção.",
    detail = detail,
    type = URI("/cancelar-solicitacao-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)