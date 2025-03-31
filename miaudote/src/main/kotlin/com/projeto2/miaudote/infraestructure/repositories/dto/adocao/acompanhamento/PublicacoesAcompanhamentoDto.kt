package com.projeto2.miaudote.infraestructure.repositories.dto.adocao.acompanhamento

import com.projeto2.miaudote.domain.entities.adocao.acompanhamento.Publicacao
import com.projeto2.miaudote.domain.entities.usuario.Usuario
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "acompanhamento_adocao")
data class PublicacoesAcompanhamentoDto(
    @Id
    @Column(name = "acompanhamento_id")
    val acompanhamentoId: UUID,

    @ManyToOne
    @JoinColumn(name = "adocao_id")
    val adocao: AdocaoDto,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "publicacao_id", referencedColumnName = "publicacao_id")
    val publicacao: Publicacao
)


@Entity
@Table(name = "adocao")
class AdocaoDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adocao_id")
    val id: Long?,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "solicitacao_adocao_id", referencedColumnName = "solicitacao_adocao_id")
    val solicitacao: SolicitacaoAdocaoDto,

    @Column(name = "data_adocao")
    val dataAdocao: LocalDateTime
)

@Entity
@Table(name = "solicitacao_adocao")
data class SolicitacaoAdocaoDto(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "solicitacao_adocao_id")
    val id: UUID?,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    val usuarioAdotante: Usuario,
)