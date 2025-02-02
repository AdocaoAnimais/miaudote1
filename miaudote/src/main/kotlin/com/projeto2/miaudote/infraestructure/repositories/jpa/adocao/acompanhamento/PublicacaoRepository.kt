package com.projeto2.miaudote.infraestructure.repositories.jpa.adocao.acompanhamento

import com.projeto2.miaudote.domain.entities.adocao.acompanhamento.Publicacao
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PublicacaoRepository :JpaRepository<Publicacao, UUID>