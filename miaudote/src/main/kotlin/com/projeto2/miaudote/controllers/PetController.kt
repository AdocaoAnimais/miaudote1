package com.projeto2.miaudote.controllers

import com.projeto2.miaudote.controllers.Adapters.Request.PetCreate
import com.projeto2.miaudote.entities.Pet
import com.projeto2.miaudote.services.PetService
import com.projeto2.miaudote.services.UsuarioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/pet")
class PetController (val service: PetService, val usuarioService: UsuarioService) {
    @GetMapping("/")
    fun obterPets(): ResponseEntity<List<Pet>> {
        return ResponseEntity(service.obterTodos(), HttpStatus.OK)
    }

    @PostMapping("/")
    fun criarPet(@RequestBody pet: PetCreate): ResponseEntity<Pet> {
        val id = pet.usuario?.toLongOrNull() ?: return ResponseEntity.badRequest().build()
        usuarioService.obterPorId(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity(service.criar(pet.toPet()), HttpStatus.OK)
    }
}