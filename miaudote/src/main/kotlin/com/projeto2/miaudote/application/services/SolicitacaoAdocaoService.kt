package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.infraestructure.repositories.SolicitacaoAdocaoRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class SolicitacaoAdocaoService(
    private val repository: SolicitacaoAdocaoRepository,
) {
    fun criar(solicitacaoAdocao: SolicitacaoAdocao): SolicitacaoAdocao {
        return repository.save(solicitacaoAdocao)
    }

    fun obterPorPetId(petId: Long): SolicitacaoAdocao? {
        return repository.findByPetId(petId)
    }

    fun obterPorId(id: UUID): SolicitacaoAdocao? {
        TODO("ALTERAR ID DA SOLICITACAO")
        return repository.findByPetId(1)
    }

    fun atualizar(solicitacaoAdocao: SolicitacaoAdocao): SolicitacaoAdocao {
        return repository.save(solicitacaoAdocao)
    }

    fun deletar(id: Long) {
        repository.deleteById(id)
    }

    fun obterPorUsuariosIdPetId(usuarioId: Long, petId: Long): SolicitacaoAdocao? {
        return repository.findByUsuarioResponsavelOrUsuarioAdotanteAndId(usuarioId, petId, usuarioId)
    }
}