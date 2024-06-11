package com.projeto2.miaudote.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class PetPostStatus(val id: String, val nome: String, val descricao: String) {
    L("L", "Logado", "Aguardando sua resposta. Verifique seu email."),
    R("R", "Responsável", "Aguardando resposta reponsável."),
    ;
}
fun Pet.getStatus(solicitacao: SolicitacaoAdocao): PetPostStatus?{
    return when {
        solicitacao.dataConfirmacaoUserResponsavel == null -> PetPostStatus.R
        solicitacao.dataConfirmacaoUserAdotante == null -> PetPostStatus.L
        else -> null
    }
}