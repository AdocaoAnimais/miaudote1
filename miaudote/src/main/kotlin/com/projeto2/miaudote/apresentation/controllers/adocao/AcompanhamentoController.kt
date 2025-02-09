package com.projeto2.miaudote.apresentation.controllers.adocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.adocao.acompanhamentoAdocao.CriarAcompanhamentoHandler
import com.projeto2.miaudote.apresentation.request.adocao.acompanhamento.PublicacaoRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RequestMapping("/acompanhamento")
@RestController
class AcompanhamentoController(
    private val criar: ProcessorHandler<CriarAcompanhamentoHandler>,
) {

    @PostMapping("/cadastrar/{petId}")
    fun cadastrar(
        @PathVariable("petId") petId: Long,
        @RequestBody publicacao: PublicacaoRequest,
        token: JwtAuthenticationToken
    ): ResponseEntity<Any> {
        val request = CriarAcompanhamentoHandler.newOrProblem(petId = petId, request = publicacao, token = token)
            .getOrElse { return ResponseEntity(it, HttpStatus.BAD_REQUEST) }

        val response = criar.process(request)
        return ResponseEntity(response, HttpStatus.OK)
    }


}