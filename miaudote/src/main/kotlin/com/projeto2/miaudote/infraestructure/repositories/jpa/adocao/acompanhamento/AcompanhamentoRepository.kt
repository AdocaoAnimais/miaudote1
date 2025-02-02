package com.projeto2.miaudote.infraestructure.repositories.jpa.adocao.acompanhamento

import com.projeto2.miaudote.domain.entities.adocao.acompanhamento.Acompanhamento
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AcompanhamentoRepository : JpaRepository<Acompanhamento, UUID>