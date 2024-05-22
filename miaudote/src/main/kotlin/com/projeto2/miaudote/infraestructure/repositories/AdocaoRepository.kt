package com.projeto2.miaudote.infraestructure.repositories

import com.projeto2.miaudote.domain.entities.Adocao
import org.springframework.data.jpa.repository.JpaRepository

interface AdocaoRepository : JpaRepository<Adocao, Long> {
    fun findByPetId(petId: Long): Adocao?
}