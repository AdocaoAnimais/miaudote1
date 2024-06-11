package com.projeto2.miaudote.application.tasks

import com.projeto2.miaudote.application.services.SolicitacaoAdocaoService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
class ExpirarAdocaoTask(private val solicitarAdocaoService: SolicitacaoAdocaoService) {
    /*
    Scheduled task que vai rodar cada dia à meia noite para verificar se tem alguma
    solicitacao nao confirmada que existe faz mais de um mes. Se existe, exclui essa
    solicitacao.
    exclui uma solicitacao de adocao nao confirmada depois de um mes
     */
    @Scheduled(cron = "0 0 0 * * *") // roda toda meia noite
    fun deletaSolicitacoesAdocaoExpiradas() {
        val dataExpiracao = LocalDateTime.now().minusMonths(1)
        val solicitacoes = solicitarAdocaoService.obterSolicitacoesDesatualizadasNaData(dataExpiracao)
        solicitarAdocaoService.deletarTodas(solicitacoes)
    }
}
