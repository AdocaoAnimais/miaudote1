package com.projeto2.miaudote.controllers

import com.projeto2.miaudote.controllers.Adapters.Request.PetCreate
import com.projeto2.miaudote.entities.Pet
import com.projeto2.miaudote.services.PetService
import com.projeto2.miaudote.services.UsuarioService
import jakarta.websocket.server.PathParam
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/pet")
class PetController (val service: PetService, val usuarioService: UsuarioService) {
    @GetMapping("/obter-pets")
    fun obterPets(): ResponseEntity<List<Pet>> {
        return ResponseEntity(service.obterTodos(), HttpStatus.OK)
    }

    @PostMapping("/{usuario}")
    fun criarPet(@RequestBody pet: PetCreate, @PathVariable("usuario") usuarioIn: String ): ResponseEntity<Pet> {
//        val id = token.name.toLongOrNull() ?: return ResponseEntity.badRequest().build()
        val usuario = usuarioService.obterUsername(usuarioIn) ?: return ResponseEntity.notFound().build()
        return ResponseEntity(service.criar(pet.toPet(usuario)), HttpStatus.OK)
    }

//    @PostMapping("/")
//    fun criarPet(@RequestBody pet: PetCreate, token: JwtAuthenticationToken ): ResponseEntity<Pet> {
//        val id = token.name.toLongOrNull() ?: return ResponseEntity.badRequest().build()
//        val usuario = usuarioService.obterPorId(id) ?: return ResponseEntity.notFound().build()
//        return ResponseEntity(service.criar(pet.toPet(usuario)), HttpStatus.OK)
//    }
}