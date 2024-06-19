package com.projeto2.miaudote.infraestructure.repositories

import com.projeto2.miaudote.domain.entities.ValidacaoEmail
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ValidacaoEmailRepository: JpaRepository<ValidacaoEmail, UUID>