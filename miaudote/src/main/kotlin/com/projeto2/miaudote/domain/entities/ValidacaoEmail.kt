package com.projeto2.miaudote.domain.entities

import jakarta.persistence.*
import java.util.*
/**
 * Representa a validação do email de um usuário.
 *
 * Contém o ID único da validação e o nome de usuário associado à validação do email.
 */
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