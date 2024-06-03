package com.projeto2.miaudote.apresentation.controllers

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.pet.AtualizarPetHandler
import com.projeto2.miaudote.application.handler.pet.CriarPetHandler
import com.projeto2.miaudote.application.handler.pet.DeletarPetHandler
import com.projeto2.miaudote.application.handler.pet.SalvarImagemHandler
import com.projeto2.miaudote.application.handler.solicitacaoAdocao.SolicitarAdocaoHandler
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.apresentation.Request.PetCreate
import com.projeto2.miaudote.apresentation.Request.PetUpdate
import com.projeto2.miaudote.domain.entities.Pet
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/pet")
class PetController(
    private val service: PetService,
    private val criarPet: ProcessorHandler<CriarPetHandler>,
    private val solicitarAdocao: ProcessorHandler<SolicitarAdocaoHandler>,
    private val editarPet: ProcessorHandler<AtualizarPetHandler>,
    private val processorDeletar: ProcessorHandler<DeletarPetHandler>,
    private val salvarImagem: ProcessorHandler<SalvarImagemHandler>
) {
    @GetMapping("/obter-pets")
    fun obterPets(): ResponseEntity<List<Pet>> {
        return ResponseEntity(service.obterTodos(), HttpStatus.OK)
    }

    @GetMapping("/obter-pets-usuario")
    fun obterMeusPets(token: JwtAuthenticationToken): ResponseEntity<List<Pet>> {
        val id = token.name.toLongOrNull() ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(service.obterPetsUsuario(id), HttpStatus.OK)
    }

    @PostMapping("/")
    fun criarPet(@RequestBody pet: PetCreate, token: JwtAuthenticationToken): ResponseEntity<Any> {
        val request = CriarPetHandler.newOrProblem(pet, token).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = criarPet.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }
    @PostMapping("/salvar-imagem/{id}")
    fun salvarImagem(@PathVariable("id") id: Long, @RequestParam("imagem") imagem: MultipartFile?, token: JwtAuthenticationToken): ResponseEntity<Any>{
        val request = SalvarImagemHandler.newOrProblem(id, imagem, token).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = salvarImagem.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }


    @PostMapping("/atualizar/{id}")
    fun atualizarPet(
        @PathVariable("id") id: Long,
        @RequestBody pet: PetUpdate,
        token: JwtAuthenticationToken
    ): ResponseEntity<Any> {
        val request = AtualizarPetHandler.newOrProblem(id, pet, token).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = editarPet.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("/deletar/{id}")
    fun deletarPet(@PathVariable("id") id: Long, token: JwtAuthenticationToken): ResponseEntity<Any> {
        val handler = DeletarPetHandler.newOrProblem(id, token).getOrElse {
            return ResponseEntity(it, HttpStatus.NOT_FOUND)
        }
        processorDeletar.process(handler).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/solicitar-adocao/{id}")
    fun solicitarAdocao(@PathVariable("id") pet: String, token: JwtAuthenticationToken): ResponseEntity<Any> {
        val request = SolicitarAdocaoHandler.newOrProblem(pet, token).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = solicitarAdocao.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }
}