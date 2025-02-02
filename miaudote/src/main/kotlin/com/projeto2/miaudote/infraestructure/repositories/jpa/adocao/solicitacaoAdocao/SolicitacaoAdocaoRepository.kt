<<<<<<<< HEAD:miaudote/src/main/kotlin/com/projeto2/miaudote/infraestructure/repositories/SolicitacaoAdocaoRepository.kt
package com.projeto2.miaudote.infraestructure.repositories
========
package com.projeto2.miaudote.infraestructure.repositories.jpa.adocao.solicitacaoAdocao
>>>>>>>> 21450eb (feat: restruturação e padronização dos arquivos por assunto):miaudote/src/main/kotlin/com/projeto2/miaudote/infraestructure/repositories/jpa/adocao/solicitacaoAdocao/SolicitacaoAdocaoRepository.kt

import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.*

interface SolicitacaoAdocaoRepository : JpaRepository<SolicitacaoAdocao, UUID> {
    @Query(
        """
        select sa from SolicitacaoAdocao sa 
        where sa.usuarioAdotante = :usuarioAdotante
        and petId = :petId"""
    )
    fun findByUsuarioAdotanteAndPetId(
        @Param("usuarioAdotante") usuarioAdotante: Long,
        @Param("petId") petId: Long
    ): SolicitacaoAdocao?

    @Query(
        """
        select sa from SolicitacaoAdocao sa 
        where sa.dataConfirmacaoUserAdotante is null 
        and (sa.dataConfirmacaoUserResponsavel < :date or sa.dataSolicitacao < :date)"""
    )
    fun findByDate(@Param("date") date: LocalDateTime): List<SolicitacaoAdocao>

    @Query(
        """
        select sa from SolicitacaoAdocao sa 
        where sa.dataConfirmacaoUserResponsavel is null 
        and sa.petId = :petdId """
    )
    fun findByPetIdAndConfirmacaoResponsavelNull(@Param("petdId") petdId: Long): List<SolicitacaoAdocao>
}