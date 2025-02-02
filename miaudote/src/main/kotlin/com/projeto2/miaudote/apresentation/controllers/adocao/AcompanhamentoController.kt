package com.projeto2.miaudote.apresentation.controllers.adocao

import com.projeto2.miaudote.apresentation.request.adocao.acompanhamento.PublicacaoRequest
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RequestMapping("/acompanhamento")
@RestController
class AcompanhamentoController {

    @PostMapping("/cadastrar/{adocaoId}")
    fun cadastrar(
        @PathVariable("adocaoId") adocaoId: String,
        @RequestBody publicacao: PublicacaoRequest,
        token: JwtAuthenticationToken
    ){


    }


}