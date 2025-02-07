package com.projeto2.miaudote.application.services.pet

import com.projeto2.miaudote.domain.entities.pet.Pet
import com.projeto2.miaudote.infraestructure.repositories.jpa.pet.PetRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class PetService (
    val repository: PetRepository
) {
    fun obterTodosDiponiveis(pageable: Pageable): Page<Pet> {
        return repository.findPetsNaoAdotados(pageable)
    }
    fun obterPetsOutrosUsuarios(id: Long, pageable: Pageable): Page<Pet> {
        return repository.findPetsOthersUsuarios(id, pageable)
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

    fun obterPetsAdotados(pageable: Pageable):  Page<Pet>{
        return repository.findPetsAdotados(pageable)
    }

    fun deletarTodos(pets: List<Pet>) {
        repository.deleteAll(pets)
    }

    fun obterPetsAdotadosUsuario(id: Long): List<Pet>? {
        return repository.findPetsAdotadosUsuario(id)
    }
}