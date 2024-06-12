package com.projeto2.miaudote.apresentation.controllers

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.usuario.AtualizarUsuarioHandler
import com.projeto2.miaudote.application.handler.usuario.CriarUsuarioHandler
import com.projeto2.miaudote.application.handler.usuario.DeletarUsuarioHandler
import com.projeto2.miaudote.application.handler.usuario.ObterUsuarioHandler
import com.projeto2.miaudote.apresentation.Request.UsuarioCreate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/usuario")
class UsuarioController(
    private val processor: ProcessorHandler<CriarUsuarioHandler>,
    private val processorDeletar: ProcessorHandler<DeletarUsuarioHandler>,
    private val processorAtualizar: ProcessorHandler<AtualizarUsuarioHandler>,
    private val processorObter: ProcessorHandler<ObterUsuarioHandler>
) {
    @GetMapping("/obter")
    fun obterPorToken(token: JwtAuthenticationToken): ResponseEntity<Any> {
        val handler = ObterUsuarioHandler.newOrProblem(token).getOrElse {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        val response = processorObter.process(handler).getOrElse {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/cadastrar")
    fun criarUsuario(@RequestBody user: UsuarioCreate): ResponseEntity<Any> {
        val handler = CriarUsuarioHandler.newOrProblem(user).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = processor.process(handler).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("/deletar")
    fun deletarUsuario(token: JwtAuthenticationToken): ResponseEntity<Void> {
        val handler = DeletarUsuarioHandler.newOrProblem(token).getOrElse {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        processorDeletar.process(handler).getOrElse {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("atualizar")
    fun atualizarUsuario(@RequestBody user: UsuarioCreate, token: JwtAuthenticationToken): ResponseEntity<Any> {
        val handler = AtualizarUsuarioHandler.newOrProblem(user, token).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = processorAtualizar.process(handler).getOrElse {
            return ResponseEntity(it, HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }
}