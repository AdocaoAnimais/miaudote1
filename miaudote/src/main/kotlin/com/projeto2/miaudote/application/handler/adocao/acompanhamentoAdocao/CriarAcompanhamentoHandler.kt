package com.projeto2.miaudote.application.handler.adocao.acompanhamentoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.adocao.AcompanhamentoService
import com.projeto2.miaudote.application.services.adocao.AdocaoService
import com.projeto2.miaudote.application.services.adocao.PublicacaoService
import com.projeto2.miaudote.application.services.adocao.toProblem
import com.projeto2.miaudote.shared.toUUID
import com.projeto2.miaudote.apresentation.request.adocao.acompanhamento.PublicacaoRequest
import com.projeto2.miaudote.domain.entities.adocao.acompanhamento.Acompanhamento
import com.projeto2.miaudote.domain.entities.adocao.acompanhamento.Publicacao
import com.projeto2.miaudote.shared.validateSize
import com.projeto2.miaudote.shared.validateType
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.net.URI
import java.sql.Blob
import java.time.LocalDate
import java.util.UUID
import javax.sql.rowset.serial.SerialBlob

class CriarAcompanhamentoProcessor(
    val adocaoService: AdocaoService,
    val publicacaoService: PublicacaoService,
    val acompanhamentoService: AcompanhamentoService
) : ProcessorHandler<CriarAcompanhamentoHandler>() {
    override fun process(handler: CriarAcompanhamentoHandler): Result<Any> {
        val adocao = adocaoService.obterPorPetId(handler.petId).toProblem().getOrElse { return Result.failure(it) }

        val publicacaoId = UUID.randomUUID()

        val publicacao = Publicacao(
            publicacaoId = publicacaoId,
            dataRegistro = LocalDate.now(),
            imageData = handler.imageData,
            descricao = handler.descricao
        )

        publicacaoService.criar(publicacao)

        val acompanhamento = Acompanhamento(
            acompanhamentoId = publicacaoId,
            adocao = adocao,
            publicacao = publicacao
        )

        acompanhamentoService.criar(acompanhamento)

        return Result.failure(publicacaoInvalida("Não é possível processar essa ação",  null))
    }
}

class CriarAcompanhamentoHandler private constructor(
    val imageData: Blob,
    val petId: Long,
    val descricao: String?,
    val usuarioId: UUID
) : RequestHandler {
    companion object {
        fun newOrProblem(
            request: PublicacaoRequest,
            petId: Long,
            token: JwtAuthenticationToken
        ): Result<CriarAcompanhamentoHandler> {
            val imageData = request.image ?: return publicacaoInvalida(
                "Foto do Pet é obrigatória para o acompanhamento.",
                null
            ).toFailure()

            request.image.validateType().getOrElse { return Result.failure(it) }
            request.image.validateSize().getOrElse { return Result.failure(it) }
            val usuarioId = token.name.toUUID().getOrElse { return Result.failure(it) }

            return Result.success(
                CriarAcompanhamentoHandler(
                    imageData = SerialBlob(imageData.bytes),
                    petId = petId,
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