package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.infraestructure.repositories.PetRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class PetService (
    val repository: PetRepository
) {
    fun obterTodos(): List<Pet> {
        return  repository.findAll()
    }
    fun obterPetsUsuario(id: Long): List<Pet>?{
        return repository.findByIdUsuario(id)
    }

    fun criar(pet: Pet): Pet {
        return repository.save(pet)
    }

    fun deletar(id: Long) {
        return repository.deleteById(id)
    }

    fun obterPorId(id: Long): Pet? {
        return repository.findById(id).getOrNull()
    }

    fun atualizar(pet: Pet): Pet {
        return repository.save(pet)
    }
}