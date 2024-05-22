package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.infraestructure.repositories.PetRepository
import org.springframework.stereotype.Service

@Service
class PetService (
    val repository: PetRepository
) {

    fun obterTodos(): List<Pet> {
        return  repository.findAll()
    }

    fun criar(pet: Pet): Pet {

        return repository.save(pet)
    }

    fun deletar(id: Long) {

        return repository.deleteById(id)
    }

    fun atualizar(pet: Pet): Pet {
        return repository.save(pet)
    }
}