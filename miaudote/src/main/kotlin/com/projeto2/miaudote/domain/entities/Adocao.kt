package com.projeto2.miaudote.domain.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID
/**
 * Representa uma adoção de pet.
 *
 * Esta classe contém os detalhes da adoção, como o ID da adoção, o ID do pet adotado,
 * o ID da solicitação de adoção e a data da adoção.
 */
@Entity
@Table(name = "adocao")
class Adocao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adocao_id")
    val id: Long?,

    @Column(name = "pet_id")
    val petId: Long,

    @Column(name = "solicitacao_adocao_id")
    val solicitacaoId: UUID,

    @Column(name = "data_adocao")
    val dataAdocao: LocalDateTime
)