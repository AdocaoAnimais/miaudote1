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
class CancelarAdocaoProcessor(
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val emailService: EmailService,
) : ProcessorHandler<CancelarAdocaoHandler>() {
    override fun process(handler: CancelarAdocaoHandler): Result<Any> {
        val adotante = usuarioService.obterUsername(handler.username).toProblem().getOrElse {
            return Result.failure(it)
        }

        val pet = petService.obterPorId(handler.petId).toProblem().getOrElse {
            return Result.failure(it)
        }

        val solicitacao = service.obterPorId(UUID.randomUUID()).toProblem().getOrElse {
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
    val username: String,
    val petId: Long,
) : RequestHandler {
    companion object {
        fun newOrProblem(petIdIn: String, username: String): Result<CancelarAdocaoHandler> {
            val petId = petIdIn.toLongOrNull() ?: return Result.failure(
                adocaoInvalida("Id do pet inválido.", null)
            )
            return Result.success(
                CancelarAdocaoHandler(
                    username = username,
                    petId = petId
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