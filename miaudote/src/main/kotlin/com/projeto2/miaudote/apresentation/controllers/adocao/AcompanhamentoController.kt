package com.projeto2.miaudote.apresentation.controllers.adocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.adocao.acompanhamentoAdocao.CriarAcompanhamentoHandler
import com.projeto2.miaudote.application.services.adocao.AcompanhamentoService
import com.projeto2.miaudote.apresentation.request.adocao.acompanhamento.PublicacaoRequest
import com.projeto2.miaudote.infraestructure.repositories.dto.adocao.acompanhamento.PublicacoesAcompanhamentoDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RequestMapping("api/acompanhamento")
@RestController
class AcompanhamentoController(
    private val criar: ProcessorHandler<CriarAcompanhamentoHandler>,
    private val service: AcompanhamentoService
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

    @GetMapping("/obter-todos")
    fun obterTodosAcompanhamentos(
        @RequestBody pageable: Pageable,
        @RequestParam("cep") endereco: String?
    ): ResponseEntity<Page<out PublicacoesAcompanhamentoDto?>> {
        val response =
            if (endereco != null) service.obterTodosPorArea(pageable, endereco) else service.obterTodos(pageable)
        return ResponseEntity(response, HttpStatus.OK)
    }
}