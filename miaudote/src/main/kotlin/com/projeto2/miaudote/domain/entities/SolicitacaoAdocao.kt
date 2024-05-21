package com.projeto2.miaudote.domain.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "selicitacao_adocao")
class SolicitacaoAdocao(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitacao_adocao_id")
    val id: Long?,

    @Column(name = "usuario_responsavel_id", nullable = false)
    val usuarioResponsavel: Long,

    @Column(name = "usuario_adotante_id", nullable = false)
    val usuarioAdotante: Long,

    @Column(name = "pet_id", nullable = false)
    val petId: Long,

    @Column(name = "data_solicitacao", nullable = false)
    val dataSolicitacao: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "data_confirmacao_usuario_adotante")
    val dataConfirmacaoUserAdotante: LocalDateTime? = null,

    @Column(name = "data_confirmacao_usuario_responsavel")
    val dataConfirmacaoUserResponsavel: LocalDateTime? = null,
) {
}