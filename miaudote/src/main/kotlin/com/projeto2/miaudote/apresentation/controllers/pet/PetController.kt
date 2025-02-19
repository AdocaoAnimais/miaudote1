package com.projeto2.miaudote.apresentation.controllers.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.pet.*
import com.projeto2.miaudote.application.handler.adocao.solicitacaoAdocao.SolicitarAdocaoHandler
import com.projeto2.miaudote.application.services.pet.PetService
import com.projeto2.miaudote.apresentation.request.pet.PetCreate
import com.projeto2.miaudote.domain.entities.pet.Pet
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
    private val salvarImagem: ProcessorHandler<SalvarImagemHandler>,
    private val obterPets: ProcessorHandler<ObterPetsHandler>,
    private val obterPetPorId: ProcessorHandler<ObterPetPorIdHandler>,
    private val obterPetsUsuario: ProcessorHandler<ObterPetsUsuarioIdHandler>
) {
    @GetMapping("/obter-pets")
    fun obterPets(token: JwtAuthenticationToken?): ResponseEntity<Any> {
        val request = ObterPetsHandler.newOrProblem(token).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = obterPets.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/obter-pets-adotados")
    fun obterPetsAdotados(): ResponseEntity<Any> {
        val response = service.obterPetsAdotados()
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/obter-pet/{id}")
    fun obterPetPorId(@PathVariable("id") id: Long, token: JwtAuthenticationToken): ResponseEntity<Any> {
        val request = ObterPetPorIdHandler.newOrProblem(id, token).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = obterPetPorId.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/obter-pets-usuario")
    fun obterMeusPets(token: JwtAuthenticationToken): ResponseEntity<Any> {
        val request = ObterPetsUsuarioIdHandler.newOrProblem(token).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = obterPetsUsuario.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/obter-pets-adotados-usuario")
    fun obterPetsAdotadosUsuario(token: JwtAuthenticationToken): ResponseEntity<List<Pet>> {
        val id = token.name.toLongOrNull() ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(service.obterPetsAdotadosUsuario(id), HttpStatus.OK)
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
    fun salvarImagem(
        @PathVariable("id") id: Long,
        @RequestParam("imagem") imagem: MultipartFile?,
        token: JwtAuthenticationToken
    ): ResponseEntity<Any> {
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
        @RequestBody pet: PetCreate,
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