package com.projeto2.miaudote.application.tasks

import com.projeto2.miaudote.application.services.SolicitacaoAdocaoService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.time.LocalDateTime


@Component
class ExpirarAdocaoTask(private val solicitarAdocaoService: SolicitacaoAdocaoService) {
    /*
    Scheduled task que vai rodar cada dia à meia noite para verificar se tem alguma
    solicitacao nao confirmada que existe faz mais de um mes. Se existe, exclui essa
    solicitacao.
     */
    @Scheduled(cron = "0 0 0 * * *") // roda toda meia noite
    fun deleteOldUnconfirmedRequests(){
        solicitarAdocaoService.expirarAdocao()
    }
}
