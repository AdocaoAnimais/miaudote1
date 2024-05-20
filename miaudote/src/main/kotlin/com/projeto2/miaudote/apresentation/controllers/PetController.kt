package com.projeto2.miaudote.apresentation.controllers

import com.projeto2.miaudote.apresentation.Request.PetCreate
import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.pet.CriarPetHandler
import com.projeto2.miaudote.application.handler.pet.CriarPetProcessor
import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.application.services.PetService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/pet")
class PetController(private val service: PetService,
                    private val criarPetProcessor: ProcessorHandler<CriarPetHandler>,
    ) {
    @GetMapping("/obter-pets")
    fun obterPets(): ResponseEntity<List<Pet>> {
        return ResponseEntity(service.obterTodos(), HttpStatus.OK)
    }

    @PostMapping("/")
    fun criarPet(@RequestBody pet: PetCreate, token: JwtAuthenticationToken): ResponseEntity<Any> {
        val request = CriarPetHandler.newOrProblem(pet, token).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = criarPetProcessor.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }
}