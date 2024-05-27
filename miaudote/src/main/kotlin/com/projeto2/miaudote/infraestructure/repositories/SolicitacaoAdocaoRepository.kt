package com.projeto2.miaudote.infraestructure.repositories

import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SolicitacaoAdocaoRepository : JpaRepository<SolicitacaoAdocao, UUID> {
    fun findByIdAndusuarioAdotante(id: Long, usuarioAdotante: Long): SolicitacaoAdocao?
}