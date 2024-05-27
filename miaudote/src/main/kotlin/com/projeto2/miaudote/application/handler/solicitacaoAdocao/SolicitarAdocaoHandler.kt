package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.*
import com.projeto2.miaudote.apresentation.Response.SolicitarAdocaoResponse
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.net.URI
import java.time.LocalDateTime

@Service
class SolicitarAdocaoProcessor(
    val emailService: EmailService,
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val adocaoService: AdocaoService,
    @Value("\${base.url}")
    private val baseUrl: String,
) : ProcessorHandler<SolicitarAdocaoHandler>() {
    override fun process(handler: SolicitarAdocaoHandler): Result<Any> {
        val pet = petService.obterPorId(handler.petId).toProblem().getOrElse { return Result.failure(it) }

        if (adocaoService.obterPorPetId(pet.id!!) != null) return Result.failure(
            solicitacaoInvalida("O pet ${pet.nome} já foi adotado.", null)
        )

        val usuarioAdotante = usuarioService.obterPorId(handler.idUsuario).toProblem()
            .getOrElse { return Result.failure(it) }

        if(service.obterPorAdotanteIdPetId(usuarioAdotante.id!!, pet.id) != null) return Result.failure(
            solicitacaoInvalida("O usuário já possui solicitação pendente para o pet ${pet.nome}.", null)
        )

        val usuarioResponsavel = usuarioService.obterPorId(pet.idUsuario).toProblem()
            .getOrElse { return Result.failure(it) }

        if (usuarioAdotante.id == usuarioResponsavel.id) return Result.failure(
            solicitacaoInvalida("O usuário não pode solicitar a adoção de seu próprio pet.", null)
        )
        val solicitacao = SolicitacaoAdocao(
            id = null,
            petId = pet.id,
            usuarioAdotante = usuarioAdotante.id!!,
            usuarioResponsavel = usuarioResponsavel.id!!,
            dataSolicitacao = LocalDateTime.now(),
            dataConfirmacaoUserAdotante = null,
            dataConfirmacaoUserResponsavel = null,
        )

        val result = service.criar(solicitacaoAdocao = solicitacao)

        val linkConfirmacao =
            "$baseUrl/solicitacao-adocao/confirmar-solicitacao/${result.id}"
        val linkCancelamento =
            "$baseUrl/solicitacao-adocao/cancelar-solicitacao/${result.id}"

        emailService.enviarEmailUsuarioResponsavel(
            responsavel = usuarioResponsavel,
            pet = pet,
            linkConfirmacaoSolicitacao = linkConfirmacao,
            linkCancelaSolicitacao = linkCancelamento,
            adotante = usuarioAdotante,
        )
        val response = SolicitarAdocaoResponse(
            id = result.petId,
            response = geraConfirmacao(pet.nome)
        )
        return Result.success(response)
    }

    private fun geraConfirmacao(nomePet: String): String {
        val confirmacao = """
            Solicitação de adoção realizada com sucesso!! 
            Já enviamos um email ao responsável pelo(a) $nomePet informando seu:
            nome, email e contato cadastrados aqui no Miaudote.
            Para que ele possa entrar em contato com você e dar continualidade a adoção!
        """.trimIndent()
        return confirmacao
    }
}

class SolicitarAdocaoHandler private constructor(
    val idUsuario: Long,
    val petId: Long,
) : RequestHandler {
    companion object {
        fun newOrProblem(petId: String?, token: JwtAuthenticationToken): Result<SolicitarAdocaoHandler> {
            val pedIdIn: Long = petId?.toLongOrNull() ?: return Result.failure(
                solicitacaoInvalida("Id do pet inválido.", null)
            )

            val id = token.name.toLongOrNull() ?: return Result.failure(
                solicitacaoInvalida("Id do usuario é inválido.", null)
            )
            return Result.success(
                SolicitarAdocaoHandler(
                    idUsuario = id,
                    petId = pedIdIn
                )
            )
        }
    }
}

private fun solicitacaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel solicitar a adoção.",
    detail = detail,
    type = URI("/solicitacao-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)