package com.projeto2.miaudote.controllers

import com.projeto2.miaudote.controllers.Adapters.Request.UsuarioCreate
import com.projeto2.miaudote.controllers.Adapters.Response.UsuarioCreateResponse
import com.projeto2.miaudote.controllers.Adapters.Response.fromEntity
import com.projeto2.miaudote.entities.Usuario
import com.projeto2.miaudote.services.JwtService
import com.projeto2.miaudote.services.UsuarioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/usuario")
class UsuarioController(val service: UsuarioService, val jwtService: JwtService) {

    @GetMapping("/")
    fun obterUsuarios(): ResponseEntity<List<Usuario>> {
        return ResponseEntity(service.obterTodos(), HttpStatus.OK)
    }

    @GetMapping("/{username}")
    fun obterPorUsername(@PathVariable("username") username: String): ResponseEntity<Usuario> {
        return ResponseEntity(service.obterUsername(username), HttpStatus.OK)
    }

    @PostMapping("/cadastrar")
    fun criarUsuario(@RequestBody user: UsuarioCreate): ResponseEntity<UsuarioCreateResponse> {
        val usuario = user.toUsuario(jwtService.passwordEncoder)
        val usuarioCriado = service.criar(usuario = usuario)
        val token = jwtService.generateToken(usuarioCriado)
        val response = usuario.fromEntity(token)
        return ResponseEntity(response, HttpStatus.OK)
    }
}