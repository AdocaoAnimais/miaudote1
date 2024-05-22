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

    fun atualizar(solicitacaoAdocao: SolicitacaoAdocao): SolicitacaoAdocao{
        return repository.save(solicitacaoAdocao)
    }

    fun deletar(id: Long) {
        repository.deleteById(id)
    }

    fun obterPorUsuariosIdPetId(usuarioId: Long, petId: Long,): SolicitacaoAdocao? {
        return repository.findByUsuarioResponsavelOrUsuarioAdotanteAndId(usuarioId, petId, usuarioId)
    }
}