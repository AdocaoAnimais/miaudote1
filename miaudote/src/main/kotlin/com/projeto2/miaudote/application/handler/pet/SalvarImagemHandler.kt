package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.pet.PetService
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import javax.sql.rowset.serial.SerialBlob
/**
 * Classe responsável por processar o salvamento de imagens para pets.
 *
 * @property petService serviço para gerenciar operações relacionadas a pets.
 */
@Component
class SalvarImagemProcessor(
    private val petService: PetService,
) : ProcessorHandler<SalvarImagemHandler>() {
    /**
     * Processa o salvamento da imagem para um pet.
     *
     * @param handler objeto contendo as informações necessárias para o salvamento.
     * @return um resultado com o pet atualizado ou um erro em caso de falha.
     */
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
        if (fileType == null || !(fileType == "image/jpeg" || fileType == "image/png")) {
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

/**
 * Classe que representa o handler para salvar imagens de pets.
 *
 * @property id ID do pet.
 * @property imagem arquivo da imagem enviado pelo usuário.
 * @property token token de autenticação do usuário.
 */
class SalvarImagemHandler private constructor(
    val id: Long,
    val imagem: MultipartFile?,
    val token: JwtAuthenticationToken,
) : RequestHandler {
    companion object {
        /**
         * Cria um handler ou retorna um problema caso os dados sejam inválidos.
         *
         * @param id ID do pet.
         * @param imagem arquivo da imagem enviado pelo usuário.
         * @param token token de autenticação do usuário.
         * @return um `Result` contendo o handler ou um erro.
         */
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
/**
 * Cria um problema para situações em que o salvamento da imagem falha.
 *
 * @param detalhe descrição detalhada do problema.
 * @param campo campo relacionado ao erro.
 * @param valor valor do campo que gerou o problema (padrão: "null").
 * @return um objeto `Problem` contendo as informações do erro.
 */
private fun salvarImagemProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel salvar a imagem",
    detail = detalhe,
    type = URI("/salvar-imagem"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)
