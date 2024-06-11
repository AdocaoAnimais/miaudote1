package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.infraestructure.repositories.SolicitacaoAdocaoRepository
import org.springframework.stereotype.Service
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

    fun obterPorAdotanteIdPetId(usuarioId: Long, petID: Long): SolicitacaoAdocao? {
        return repository.findByUsuarioAdotanteAndPetId(usuarioId, petID)
    }

    fun obterSolicitacoesDesatualizadasNaData(date: LocalDateTime): List<SolicitacaoAdocao> {
        return repository.findByDate(date)
    }

    fun deletarTodas(solicitacoes: List<SolicitacaoAdocao>) {
        repository.deleteAll(solicitacoes)
    }
}