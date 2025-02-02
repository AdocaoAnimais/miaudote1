package com.projeto2.miaudote.infraestructure.repositories.jpa.adocao

import com.projeto2.miaudote.domain.entities.Adocao
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AdocaoRepository : JpaRepository<Adocao, Long> {
    fun findByPetId(petId: Long): Adocao?
    fun findBySolicitacaoId(solicitacaoId: UUID): Adocao?
}