package com.projeto2.miaudote.domain.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "adocao")
class Adocao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adocao_id")
    val id: Long?,

    @Column(name = "pet_id")
    val petId: Long,

    @Column(name = "data_adocao")
    val dataAdocao: LocalDateTime
)