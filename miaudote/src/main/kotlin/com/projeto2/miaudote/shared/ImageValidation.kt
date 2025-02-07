package com.projeto2.miaudote.shared

import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
import java.net.URI

fun MultipartFile?.validateType(): Result<MultipartFile?> {
    val fileType = this?.contentType
    if (fileType == null || !(fileType == "image/jpeg" || fileType == "image/png")) {
        return salvarImagemProblem(
            detalhe = "Imagem em formato incorreto",
            campo = "imageData"
        ).toFailure()
    }
    return Result.success(this)
}

fun MultipartFile?.validateSize(): Result<MultipartFile?> {
    val maxFileSize = 16000000 // 16MB
    if (this != null && this.size > maxFileSize) {
        return salvarImagemProblem(
            detalhe = "Imagem excede o tamanho de 16MB",
            campo = "compressedImageData",
        ).toFailure()
    }
    return Result.success(this)
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
