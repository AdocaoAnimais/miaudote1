package com.projeto2.miaudote.apresentation.controllers

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.usuario.CriarUsuarioHandler
import com.projeto2.miaudote.apresentation.Request.UsuarioCreate
import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.application.services.JwtService
import com.projeto2.miaudote.application.services.UsuarioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/usuario")
class UsuarioController(
    private val processor: ProcessorHandler<CriarUsuarioHandler>
) {

    // @GetMapping("/")
    // fun obterUsuarios(): ResponseEntity<List<Usuario>> {
    //    return ResponseEntity(service.obterTodos(), HttpStatus.OK)
    // }

    // @GetMapping("/{username}")
    // fun obterPorUsername(@PathVariable("username") username: String): ResponseEntity<Usuario> {
    //    return ResponseEntity(service.obterUsername(username), HttpStatus.OK)
    // }

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
}