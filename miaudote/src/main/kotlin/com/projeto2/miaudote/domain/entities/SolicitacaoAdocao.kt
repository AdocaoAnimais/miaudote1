package com.projeto2.miaudote.domain.entities

import com.projeto2.miaudote.application.problems.Problem
import jakarta.persistence.*
import org.springframework.http.HttpStatus
import java.net.URI
import java.time.LocalDateTime

@Entity
@Table(name = "selicitacao_adocao")
data class SolicitacaoAdocao(

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
    fun update(
        dataConfirmacaoUserAdotante: LocalDateTime?,
        dataConfirmacaoUserResponsavel: LocalDateTime?
    ): SolicitacaoAdocao {
        return this.copy(
            dataConfirmacaoUserResponsavel = dataConfirmacaoUserResponsavel,
            dataConfirmacaoUserAdotante = dataConfirmacaoUserAdotante
        )
    }
}

fun SolicitacaoAdocao?.toProblem(): Result<SolicitacaoAdocao> {
    if (this != null) return Result.success(this)
    return Result.failure(
        Problem(
            title = "Pet não encontrado",
            detail = "O pet com id informado não esta cadastrado",
            type = URI("/obter-pet-por-id"),
            status = HttpStatus.BAD_REQUEST,
            extra = null
        )
    )
}