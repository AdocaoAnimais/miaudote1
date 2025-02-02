package com.projeto2.miaudote.infraestructure.repositories.jpa.usuario

import com.projeto2.miaudote.domain.entities.usuario.ValidacaoEmail
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ValidacaoEmailRepository: JpaRepository<ValidacaoEmail, UUID>