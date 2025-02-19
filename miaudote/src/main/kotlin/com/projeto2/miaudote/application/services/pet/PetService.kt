package com.projeto2.miaudote.application.services.pet

import com.projeto2.miaudote.domain.entities.pet.Pet
import com.projeto2.miaudote.infraestructure.repositories.jpa.pet.PetRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class PetService (
    val repository: PetRepository
) {
    fun obterTodosDiponiveis(): List<Pet>? {
        return repository.findPetsNaoAdotados()
    }
    fun obterPetsOutrosUsuarios(id: Long): List<Pet>?{
        return repository.findPetsOthersUsuarios(id)
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

    fun obterPetsAdotados(): List<Pet>?{
        return repository.findPetsAdotados()
    }

    fun deletarTodos(pets: List<Pet>) {
        repository.deleteAll(pets)
    }

    fun obterPetsAdotadosUsuario(id: Long): List<Pet>? {
        return repository.findPetsAdotadosUsuario(id)
    }
}