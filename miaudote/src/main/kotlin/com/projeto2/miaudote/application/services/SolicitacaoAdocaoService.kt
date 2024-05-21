package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.infraestructure.repositories.SolicitacaoAdocaoRepository
import org.springframework.stereotype.Service

@Service
class SolicitacaoAdocaoService(
    private val repository: SolicitacaoAdocaoRepository,
) {
    fun criar(solicitacaoAdocao: SolicitacaoAdocao): SolicitacaoAdocao{
        return repository.save(solicitacaoAdocao)
    }

    fun obterPorPetId(petId: Long): SolicitacaoAdocao? {
        return repository.findByPetId(petId)
    }
}