package com.projeto2.miaudote.domain.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
/**
 * Enumeração que representa os possíveis status de um pet em relação a uma solicitação de adoção.
 *
 * - "L" para Logado: O pet está aguardando a resposta do usuário adotante. Verifique seu email.
 * - "R" para Responsável: O pet está aguardando a resposta do responsável pela adoção.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class PetPostStatus(val id: String, val nome: String, val descricao: String) {
    L("L", "Logado", "Aguardando sua resposta. Verifique seu email."),
    R("R", "Responsável", "Aguardando resposta reponsável."),
    ;
}
/**
 * Obtém o status de um pet com base na solicitação de adoção.
 *
 * - Se a solicitação do responsável ainda não foi confirmada, retorna [PetPostStatus.R].
 * - Se a solicitação do adotante ainda não foi confirmada, retorna [PetPostStatus.L].
 * - Caso contrário, retorna null.
 *
 * @param solicitacao A solicitação de adoção associada ao pet.
 * @return O status atual do pet na solicitação ou null, caso nenhum status aplicável.
 */
fun Pet.getStatus(solicitacao: SolicitacaoAdocao): PetPostStatus?{
    return when {
        solicitacao.dataConfirmacaoUserResponsavel == null -> PetPostStatus.R
        solicitacao.dataConfirmacaoUserAdotante == null -> PetPostStatus.L
        else -> null
    }
}

/**
 * Classe que representa o status de um responsável em relação a solicitações pendentes.
 *
 * A classe contém uma descrição e nome que indicam o estado de solicitações não respondidas.
 */
class StatusResponsavel private constructor(
    val descricao: String,
    val nome: String
) {
    companion object {
        /**
         * Gera um status para o responsável, com base no número de solicitações pendentes.
         *
         * A descrição do status varia dependendo se há uma ou mais solicitações pendentes.
         *
         * @param quantidadeSolicitacoes O número de solicitações pendentes.
         * @return Um objeto [StatusResponsavel] com a descrição e nome apropriados.
         */
        fun gerarStatus(quantidadeSolicitacoes: Int): StatusResponsavel {
            val solicitacao =  if(quantidadeSolicitacoes > 1) "solicitações pendentes" else "solicitação pendente"

            return StatusResponsavel(
                descricao = """Você possui $quantidadeSolicitacoes $solicitacao. Verifique seu email.""",
                nome = "Solicitações não respondidas"
            )
        }
    }
}