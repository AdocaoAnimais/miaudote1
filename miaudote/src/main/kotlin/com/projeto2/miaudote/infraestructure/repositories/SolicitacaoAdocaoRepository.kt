package com.projeto2.miaudote.infraestructure.repositories

import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.*

interface SolicitacaoAdocaoRepository : JpaRepository<SolicitacaoAdocao, UUID> {
    fun findByUsuarioAdotanteAndPetId(usuarioAdotante: Long, petId: Long): SolicitacaoAdocao?
    @Query("""
        select sa from SolicitacaoAdocao sa 
        where sa.dataConfirmacaoUserAdotante is null 
        and (sa.dataConfirmacaoUserResponsavel > :date or sa.dataSolicitacao > :date)""")
    fun findByDate(@Param("date") date: LocalDateTime): List<SolicitacaoAdocao>
}