package com.projeto2.miaudote.apresentation.controllers.adocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.adocao.solicitacaoAdocao.CancelarAdocaoHandler
import com.projeto2.miaudote.application.handler.adocao.solicitacaoAdocao.CancelarSolicitacaoHandler
import com.projeto2.miaudote.application.handler.adocao.solicitacaoAdocao.ConfirmarAdocaoHandler
import com.projeto2.miaudote.application.handler.adocao.solicitacaoAdocao.ConfirmarSolicitacaoHandler
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
    private val cancelarAdocaoProcessor: ProcessorHandler<CancelarAdocaoHandler>,
    private val confirmarAdocaoProcessor: ProcessorHandler<ConfirmarAdocaoHandler>
) {

    @GetMapping("/confirmar-solicitacao/{solicitacaoId}")
    fun confirmarSolicitacao(
        @PathVariable("solicitacaoId") solicitacaoId: String,
    ): ResponseEntity<Any> {
        val request = ConfirmarSolicitacaoHandler.newOrProblem(
            solicitacaoIdIn = solicitacaoId,
        ).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        val response = confirmarSolicitacaoProcessor.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/cancelar-solicitacao/{solicitacaoId}")
    fun cancelarSolicitacao(
        @PathVariable("solicitacaoId") solicitacaoId: String,
    ): ResponseEntity<Any> {
        val request = CancelarSolicitacaoHandler.newOrProblem(
            solicitacaoIdIn = solicitacaoId,
        ).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        val response = cancelarSolicitacaoProcessor.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/confirmar-adocao/{solicitacaoId}")
    fun confirmarAdocao(
        @PathVariable("solicitacaoId") solicitacaoId: String,
    ): ResponseEntity<Any> {
        val request = ConfirmarAdocaoHandler.newOrProblem(
            solicitacaoIdIn = solicitacaoId,
        ).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        val response = confirmarAdocaoProcessor.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/cancelar-adocao/{solicitacaoId}")
    fun cancelarAdocao(
        @PathVariable("solicitacaoId") solicitacaoId: String,
    ): ResponseEntity<Any> {
        val request = CancelarAdocaoHandler.newOrProblem(
            solicitacaoAdocaoIdIn = solicitacaoId,
        ).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        val response = cancelarAdocaoProcessor.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(response, HttpStatus.OK)
    }
}