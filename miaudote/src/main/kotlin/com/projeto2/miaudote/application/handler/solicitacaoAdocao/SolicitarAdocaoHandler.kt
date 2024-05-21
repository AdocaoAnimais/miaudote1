package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.EmailService
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.services.SolicitacaoAdocaoService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.apresentation.Response.SolicitarAdocaoResponse
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.domain.entities.toProblem
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
) : ProcessorHandler<SolicitarAdocaoHandler>() {
    override fun process(handler: SolicitarAdocaoHandler): Result<Any> {
        val usuarioAdotante = usuarioService.obterPorId(handler.idUsuario).toProblem()
            .getOrElse { return Result.failure(it) }

        val pet = petService.obterPorId(handler.petId).toProblem().getOrElse { return Result.failure(it) }

        val usuarioResponsavel = usuarioService.obterPorId(pet.idUsuario).toProblem()
            .getOrElse { return Result.failure(it) }

        if (usuarioAdotante.username == usuarioResponsavel.username) return Result.failure(
            Problem(
                title = "Não foi possivel solicitar a adoção.",
                detail = "O usuário adotante não pode ser o usuário responsavel pelo animal.",
                type = URI("/solicitacao-adocao"),
                status = HttpStatus.BAD_REQUEST,
                extra = null
            )
        )

        val link = "google.com"

        emailService.enviarEmailUsuarioResponsavel(
            responsavel = usuarioResponsavel,
            pet = pet,
            linkConfirmacaoSolicitacao = link,
            adotante = usuarioAdotante,
        )

        val solicitacao = SolicitacaoAdocao(
            id = null,
            petId = pet.id!!,
            usuarioAdotante = usuarioAdotante.id!!,
            usuarioResponsavel = usuarioResponsavel.id!!,
            dataSolicitacao = LocalDateTime.now(),
            dataConfirmacaoUserAdotante = null,
            dataConfirmacaoUserResponsavel = null,
        )

        val result = service.criar(solicitacaoAdocao = solicitacao)
        val response = SolicitarAdocaoResponse(
            id = result.petId,
        )

        return Result.success(response)
    }

}

class SolicitarAdocaoHandler private constructor(
    val idUsuario: Long,
    val petId: Long,
) : RequestHandler {
    companion object {
        fun newOrProblem(petId: String?, token: JwtAuthenticationToken): Result<SolicitarAdocaoHandler> {
            val pedIdIn: Long = petId?.toLongOrNull() ?: return Result.failure(
                Problem(
                    title = "Não foi possivel solicitar a adoção.",
                    detail = "Id do pet inválido.",
                    type = URI("/solicitacao-adocao"),
                    status = HttpStatus.BAD_REQUEST,
                    extra = null
                )
            )

            val id = token.name.toLongOrNull() ?: return Result.failure(
                Problem(
                    title = "Não foi possivel solicitar a adoção.",
                    detail = "Id do usuario é inválido.",
                    type = URI("/solicitacao-adocao"),
                    status = HttpStatus.BAD_REQUEST,
                    extra = null
                )
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