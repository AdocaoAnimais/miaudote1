package com.projeto2.miaudote.apresentation.request.adocao.acompanhamento

import org.springframework.web.multipart.MultipartFile

class PublicacaoRequest(
    val image: MultipartFile?,
    val descricao: String?
)
