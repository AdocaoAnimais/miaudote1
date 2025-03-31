package com.projeto2.miaudote.infraestructure.repositories.jpa.adocao.acompanhamento

import com.projeto2.miaudote.infraestructure.repositories.dto.adocao.acompanhamento.PublicacoesAcompanhamentoDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*


interface PublicacoesAcompanhamentoRepository : JpaRepository<PublicacoesAcompanhamentoDto, UUID> {

    @Query(
        """
            SELECT p FROM PublicacoesAcompanhamentoDto p
            JOIN p.adocao a
            JOIN a.solicitacao s
            JOIN s.usuarioAdotante u
            WHERE u.endereco IS NOT NULL
            ORDER BY ABS(CAST(u.endereco AS int) - CAST(:endereco AS int)) ASC
        """
    )
    fun buscarPublicacoesPorProximidade(
        @Param("endereco") cep: String?, pageable: Pageable?
    ): Page<PublicacoesAcompanhamentoDto?>

}