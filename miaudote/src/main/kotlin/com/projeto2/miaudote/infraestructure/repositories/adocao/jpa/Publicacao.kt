package com.projeto2.miaudote.infraestructure.repositories.adocao.jpa

import Publicacao
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PublicacaoRepository :JpaRepository<Publicacao, UUID>