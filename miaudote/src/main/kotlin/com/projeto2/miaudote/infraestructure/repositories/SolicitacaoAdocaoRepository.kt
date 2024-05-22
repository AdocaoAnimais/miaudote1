package com.projeto2.miaudote.infraestructure.repositories

import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import org.springframework.data.jpa.repository.JpaRepository

interface SolicitacaoAdocaoRepository : JpaRepository<SolicitacaoAdocao, Long> {
    fun findByPetId(id: Long): SolicitacaoAdocao?
    fun findByUsuarioResponsavelOrUsuarioAdotanteAndId(
        usuarioResponsavelId: Long,
        petId: Long,
        usuarioAdotanteId: Long
    ): SolicitacaoAdocao?
}