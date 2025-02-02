package com.projeto2.miaudote.domain.entities.adocao

import Publicacao
import com.projeto2.miaudote.domain.entities.Adocao
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "acompanhamento_adocao")
class Acompanhamento (
    @Id
    @Column(name = "acompanhamento_id")
    val acompanhamentoId: UUID,

    @ManyToOne
    @JoinColumn(name = "adocao_id")
    val adocao: Adocao,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "publicacao_id", referencedColumnName = "publicacao_id")
    val publicacao: Publicacao
)

class AcompanhamentoResumeDTO (
    val acompanhamentoId: UUID,
    val publicacao: Publicacao
)