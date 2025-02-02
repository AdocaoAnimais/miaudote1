package com.projeto2.miaudote.application.handler.acompanhamentoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.shared.toUUID
import com.projeto2.miaudote.apresentation.Request.PublicacaoRequest
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.util.UUID

class CriarAcompanhamentoProcessor : ProcessorHandler<CriarAcompanhamentoHandler>() {
    override fun process(handler: CriarAcompanhamentoHandler): Result<Any> {
        return Result.failure(publicacaoInvalida("Não é possível processar essa ação",  null))
    }
}

class CriarAcompanhamentoHandler private constructor(
    val imageData: MultipartFile,
    val adocaoId: UUID,
    val descricao: String?,
    val usuarioId: UUID
) : RequestHandler {
    companion object {
        fun newOrProblem(
            request: PublicacaoRequest,
            adocaoIdRequest: String,
            token: JwtAuthenticationToken
        ): Result<CriarAcompanhamentoHandler> {
            val imageData = request.image ?: return publicacaoInvalida(
                "Foto do Pet é obrigatória para o acompanhamento.",
                null
            ).toFailure()
            val usuarioId = token.name.toUUID().getOrElse { return Result.failure(it) }
            val adocaoId = adocaoIdRequest.toUUID().getOrElse { return Result.failure(it) }
            return Result.success(
                CriarAcompanhamentoHandler(
                    imageData = imageData,
                    adocaoId = adocaoId,
                    descricao = request.descricao,
                    usuarioId = usuarioId
                )
            )
        }
    }
}

private fun publicacaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel registrar o acompanhamento.",
    detail = detail,
    type = URI("/criar-acompanhamento-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)