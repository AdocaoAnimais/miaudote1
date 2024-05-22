package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.Adocao
import com.projeto2.miaudote.infraestructure.repositories.AdocaoRepository
import org.springframework.stereotype.Service

@Service
class AdocaoService(
    private val respository: AdocaoRepository,
) {
    fun obterPorPetId(petId: Long): Adocao?{
        return respository.findByPetId(petId)
    }

    fun criar(adocao: Adocao): Adocao{
        return respository.save(adocao)
    }
}
