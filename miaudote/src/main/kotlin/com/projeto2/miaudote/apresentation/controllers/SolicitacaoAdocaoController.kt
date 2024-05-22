package com.projeto2.miaudote.apresentation.controllers

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.solicitacaoAdocao.CancelarAdocaoHandler
import com.projeto2.miaudote.application.handler.solicitacaoAdocao.CancelarSolicitacaoHandler
import com.projeto2.miaudote.application.handler.solicitacaoAdocao.ConfirmarSolicitacaoHandler
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/solicitacao-adocao")
@RestController
class SolicitacaoAdocaoController(
    private val confirmarSolicitacaoProcessor: ProcessorHandler<ConfirmarSolicitacaoHandler>,
    private val cancelarSolicitacaoProcessor: ProcessorHandler<CancelarSolicitacaoHandler>,
    private val cancelarAdocaoProcessor: ProcessorHandler<CancelarAdocaoHandler>
) {

    @GetMapping("/confirmar-solicitacao/{username}/{petId}/")
    fun confirmarSolicitacao(
        @PathVariable("username") username: String,
        @PathVariable("petId") petId: String
    ): ResponseEntity<Any> {
        val request = ConfirmarSolicitacaoHandler.newOrProblem(
            petIdIn = petId,
            username = username
        ).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        val response = confirmarSolicitacaoProcessor.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/cancelar-solicitacao/{username}/{petId}")
    fun cancelarSolicitacao(
        @PathVariable("username") username: String,
        @PathVariable("petId") petId: String
    ): ResponseEntity<Any> {
        val request = CancelarSolicitacaoHandler.newOrProblem(
            petIdIn = petId,
            username = username
        ).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        val response = cancelarSolicitacaoProcessor.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/confirmar-adocao/{username}/{petId}")
    fun confirmarAdocao(
        @PathVariable("username") username: String,
        @PathVariable("petId") petId: String
    ): ResponseEntity<Any> {
        val request = ConfirmarSolicitacaoHandler.newOrProblem(
            petIdIn = petId,
            username = username
        ).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        val response = confirmarSolicitacaoProcessor.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/cancelar-adocao/{username}/{petId}")
    fun cancelarAdocao(
        @PathVariable("username") username: String,
        @PathVariable("petId") petId: String
    ): ResponseEntity<Any> {
        val request = CancelarAdocaoHandler.newOrProblem(
            petIdIn = petId,
            username = username
        ).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        val response = cancelarAdocaoProcessor.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(response, HttpStatus.OK)
    }
}