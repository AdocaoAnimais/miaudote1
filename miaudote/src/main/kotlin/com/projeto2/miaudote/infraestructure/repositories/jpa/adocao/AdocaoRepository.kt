<<<<<<<< HEAD:miaudote/src/main/kotlin/com/projeto2/miaudote/infraestructure/repositories/AdocaoRepository.kt
package com.projeto2.miaudote.infraestructure.repositories
========
package com.projeto2.miaudote.infraestructure.repositories.jpa.adocao
>>>>>>>> 21450eb (feat: restruturação e padronização dos arquivos por assunto):miaudote/src/main/kotlin/com/projeto2/miaudote/infraestructure/repositories/jpa/adocao/AdocaoRepository.kt

import com.projeto2.miaudote.domain.entities.Adocao
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AdocaoRepository : JpaRepository<Adocao, Long> {
    fun findByPetId(petId: Long): Adocao?
    fun findBySolicitacaoId(solicitacaoId: UUID): Adocao?
}