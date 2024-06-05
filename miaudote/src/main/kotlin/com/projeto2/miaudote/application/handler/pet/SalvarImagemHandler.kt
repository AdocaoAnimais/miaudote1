package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.ImagemService
import com.projeto2.miaudote.application.services.PetService
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import javax.sql.rowset.serial.SerialBlob

@Component
class SalvarImagemProcessor(
    private val petService: PetService,
) : ProcessorHandler<SalvarImagemHandler>() {

    override fun process(handler: SalvarImagemHandler): Result<Any> {
        val id = handler.token.name.toLongOrNull() ?: return salvarImagemProblem(
            "Id do usuário não encontrado.",
            "ID"
        ).toFailure()

        val petExistente = petService.obterPorId(handler.id) ?: return salvarImagemProblem(
            "Pet não encontrado",
            "pet"
        ).toFailure()

        if (petExistente.idUsuario != id) {
            return salvarImagemProblem(
                "Usuário não tem permissão para atualizar este pet",
                "usuarioId",
                id.toString()
            ).toFailure()
        }

        val imageData = handler.imagem?.bytes ?: return salvarImagemProblem(
            "Nenhuma imagem uploaded",
            campo = "imageData"
        ).toFailure()

        val fileType = handler.imagem.contentType
        if (fileType == null || !fileType.startsWith("image/")) {
            return salvarImagemProblem(
                detalhe = "Imagem em formato incorreto",
                campo = "imageData"
            ).toFailure()
        }

        val maxFileSize = 16000000 // 16MB
        if (handler.imagem.size > maxFileSize ) {
            return salvarImagemProblem(
                detalhe = "Imagem excede o tamanho de 16MB",
                campo = "compressedImageData",
            ).toFailure()
        }

        val blob = SerialBlob(imageData)
        val petAtualizado = petExistente.copy(
            imageData = blob
        )

        val result = petService.atualizar(petAtualizado)
        return Result.success(result)
    }
}


class SalvarImagemHandler private constructor(
    val id: Long,
    val imagem: MultipartFile?,
    val token: JwtAuthenticationToken,
) : RequestHandler {
    companion object {
        fun newOrProblem(id: Long, imagem: MultipartFile?, token: JwtAuthenticationToken): Result<SalvarImagemHandler> {
            val userId = token.name.toLongOrNull() ?: return Result.failure(
                salvarImagemProblem(
                    "Id do usuário não encontrado.",
                    "usuarioId",
                )
            )
            if (id <= 0) return Result.failure(
                salvarImagemProblem(
                    "Campo 'petId' não pode ser menor ou igual a zero",
                    "petId",
                    id.toString()
                )
            )
            val response = SalvarImagemHandler(
                id = id,
                imagem = imagem,
                token = token
            )
            return Result.success(response)
        }
    }
}

private fun salvarImagemProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel salvar a imagem",
    detail = detalhe,
    type = URI("/salvar-imagem"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)
