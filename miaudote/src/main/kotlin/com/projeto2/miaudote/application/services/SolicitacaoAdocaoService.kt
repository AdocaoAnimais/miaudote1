package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.infraestructure.repositories.SolicitacaoAdocaoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class SolicitacaoAdocaoService(
    private val repository: SolicitacaoAdocaoRepository,
) {
    fun criar(solicitacaoAdocao: SolicitacaoAdocao): SolicitacaoAdocao {
        return repository.save(solicitacaoAdocao)
    }

    fun obterPorId(id: UUID): SolicitacaoAdocao? {
        return repository.findById(id).getOrNull()
    }

    fun atualizar(solicitacaoAdocao: SolicitacaoAdocao): SolicitacaoAdocao {
        return repository.save(solicitacaoAdocao)
    }

    fun deletar(id: UUID) {
        repository.deleteById(id)
    }

    fun obterPorAdotanteIdPetId(usuarioId: Long, petID: Long): SolicitacaoAdocao? { return repository.findByUsuarioAdotanteAndPetId(petID, usuarioId)
    }
    @Transactional
    fun expirarAdocao() { // exclui uma solicitacao de adocao nao confirmada depois de um mes
        val oneMonthAgo = LocalDateTime.now().minusMonths(1)
        val oldUnconfirmedRequests = repository.findByDate(oneMonthAgo)
        repository.deleteAll(oldUnconfirmedRequests)
    }
}