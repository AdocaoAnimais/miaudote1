package com.projeto2.miaudote.domain.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "validacao_email")
class ValidacaoEmail (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "validacao_email_id")
    val id: UUID?,

    @Column(name = "username", nullable = false)
    val username: String,
    )