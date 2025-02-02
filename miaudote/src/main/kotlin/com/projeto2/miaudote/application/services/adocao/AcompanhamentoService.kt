package com.projeto2.miaudote.application.services.adocao

import com.projeto2.miaudote.domain.entities.adocao.acompanhamento.Acompanhamento
import com.projeto2.miaudote.infraestructure.repositories.jpa.adocao.acompanhamento.AcompanhamentoRepository
import org.springframework.stereotype.Service

@Service
class AcompanhamentoService(
    val acompanhamentoRepository: AcompanhamentoRepository
) {

    fun criar(acompanhamento: Acompanhamento): Acompanhamento{
        return acompanhamentoRepository.save(acompanhamento)
    }

}