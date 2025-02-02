package com.projeto2.miaudote.application.services.adocao

import com.projeto2.miaudote.domain.entities.adocao.acompanhamento.Publicacao
import com.projeto2.miaudote.infraestructure.repositories.jpa.adocao.acompanhamento.PublicacaoRepository
import org.springframework.stereotype.Service

@Service
class PublicacaoService(
    val publicacaoRepository: PublicacaoRepository
) {
    fun criar(publicacao: Publicacao): Publicacao {
        return publicacaoRepository.save(publicacao)
    }
}