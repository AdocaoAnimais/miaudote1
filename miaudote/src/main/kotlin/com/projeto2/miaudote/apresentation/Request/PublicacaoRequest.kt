package com.projeto2.miaudote.apresentation.Request

import org.springframework.web.multipart.MultipartFile

class PublicacaoRequest(
    val image: MultipartFile?,
    val descricao: String?
)
