package com.projeto2.miaudote.application.services.adocao

import com.projeto2.miaudote.domain.entities.adocao.acompanhamento.Acompanhamento
import com.projeto2.miaudote.infraestructure.repositories.dto.adocao.acompanhamento.PublicacoesAcompanhamentoDto
import com.projeto2.miaudote.infraestructure.repositories.jpa.adocao.acompanhamento.AcompanhamentoRepository
import com.projeto2.miaudote.infraestructure.repositories.jpa.adocao.acompanhamento.PublicacoesAcompanhamentoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AcompanhamentoService(
    val acompanhamentoRepository: AcompanhamentoRepository,
    val publicacoesAcompanhamentoRepository: PublicacoesAcompanhamentoRepository
) {

    fun criar(acompanhamento: Acompanhamento): Acompanhamento{
        return acompanhamentoRepository.save(acompanhamento)
    }
    fun obterTodos(pageable: Pageable): Page<PublicacoesAcompanhamentoDto>{
        return publicacoesAcompanhamentoRepository.findAll(pageable)
    }

    fun obterTodosPorArea(pageable: Pageable, endereco: String): Page<PublicacoesAcompanhamentoDto?>{
        return  publicacoesAcompanhamentoRepository.buscarPublicacoesPorProximidade(endereco, pageable)
    }
}