package com.projeto2.miaudote.domain.entities

import com.projeto2.miaudote.application.problems.Problem
import jakarta.persistence.*
import org.springframework.http.HttpStatus
import java.net.URI
import java.time.LocalDateTime
import java.util.*
/**
 * Representa uma solicitação de adoção de pet.
 *
 * Esta classe contém detalhes da solicitação, incluindo os usuários responsáveis, o pet solicitado e as datas de confirmação.
 */
@Entity
@Table(name = "solicitacao_adocao")
data class SolicitacaoAdocao(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "solicitacao_adocao_id")
    val id: UUID?,

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
    /**
     * Método para atualizar a solicitação de adoção com as novas datas de confirmação.
     *
     * @param dataConfirmacaoUserAdotante A data de confirmação do adotante.
     * @param dataConfirmacaoUserResponsavel A data de confirmação do responsável pelo pet.
     * @return Uma nova instância de `SolicitacaoAdocao` com as datas atualizadas.
     */
    fun update(
        dataConfirmacaoUserAdotante: LocalDateTime?,
        dataConfirmacaoUserResponsavel: LocalDateTime?
    ): SolicitacaoAdocao {
        return this.copy(
            dataConfirmacaoUserResponsavel = dataConfirmacaoUserResponsavel,
            dataConfirmacaoUserAdotante = dataConfirmacaoUserAdotante,
        )
    }
}
/**
 * Função de extensão para verificar se uma solicitação de adoção existe e retornar um problema caso não.
 *
 * @return Um `Result` contendo a solicitação de adoção se encontrada ou um erro caso contrário.
 */
fun SolicitacaoAdocao?.toProblem(): Result<SolicitacaoAdocao> {
    if (this != null) return Result.success(this)
    return Result.failure(
        Problem(
            title = "Solicitação de adoção não encontrada",
            detail = "Não existe solicitação para o Id informado.",
            type = URI("/obter-solicitacao-por-id"),
            status = HttpStatus.BAD_REQUEST,
            extra = null
        )
    )
}