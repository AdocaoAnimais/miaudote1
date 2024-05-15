package com.projeto2.miaudote.controllers

import com.projeto2.miaudote.entities.Usuario
import com.projeto2.miaudote.services.UsuarioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/usuario")
class UsuarioController (val service: UsuarioService) {

    @GetMapping("/")
    fun obterUsuarios(): ResponseEntity<List<Usuario>> {
        return ResponseEntity(service.obterTodos(), HttpStatus.OK)
    }

    @PostMapping("/")
    fun criarUsuario(@RequestBody user: Usuario): ResponseEntity<Usuario> {
        return ResponseEntity(service.criar(usuario = user), HttpStatus.OK)
    }

}