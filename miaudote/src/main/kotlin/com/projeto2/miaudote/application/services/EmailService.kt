package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.domain.entities.Usuario
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    val mailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    private val from: String,
) {
    fun enviarEmail(to: String, subject: String, conteudo: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = subject
        message.text = conteudo
        message.from = from
        mailSender.send(message)
    }

    fun enviarEmailUsuarioAdotante(
        adotante: Usuario,
        pet: Pet,
        linkConfirmaAdocao: String,
        linkCancelaAdocao: String,
        responsavel: Usuario
    ) {
        val titulo = "[MIAUDOTE] Confirmação da Adoção - ${pet.nome}"
        val conteudo = """Prezado ${adotante.nome} ${adotante.sobrenome},
            Confirmamos o recebimento de sua solicitação de adoção para adotar o(a) ${pet.nome}.
            
            Segue abaixo o contato do tutor responsável pelo animal, você deve entrar em contato para dar continualidade à adoção:

            Nome do Tutor: ${responsavel.nome}
            E-mail do Tutor: ${responsavel.email}
            Telefone do Tutor: ${responsavel.contato}
            
            Para CONFIRMAR o processo de adoção de ${pet.nome}, clique no link abaixo:
            
            Apenas clique no link abaixo se a ADOÇÃO OCORREU COM SUCESSO e o(a) ${pet.nome} já estiver no seu novo lar!!
            
                CONFIRMAR ADOÇÃO: 
                $linkConfirmaAdocao
            
            ----------------------------------------------------------------
            
            QUERO CANCELAR A ADOÇÃO RECEBIDA: 
                Caso não queira proceder com a solicitação da adoção de ${pet.nome} clique no link abaixo:
                
                Link para CANCELAR a adoção do pet que você solicitou: 
                $linkCancelaAdocao
            
            Não reponda este email.
            """.trimIndent()

        enviarEmail(
            to = adotante.email,
            subject = titulo,
            conteudo = conteudo,
        )
    }

    fun enviarEmailUsuarioResponsavel(
        responsavel: Usuario,
        pet: Pet,
        linkConfirmacaoSolicitacao: String,
        linkCancelaSolicitacao: String,
        adotante: Usuario
    ) {
        val titulo = "[MIAUDOTE] Solicitação de Adoção - ${pet.nome}"
        val conteudo = """
            Prezado ${responsavel.nome} ${responsavel.sobrenome},
            recebimento uma solicitação de adoção para adotar o(a) ${pet.nome}.
            
            Segue abaixo o contato do usuario interessado em adotar o(a) ${pet.nome}.
            Você deve entrar em contato para dar continualidade à adoção:
  
            Nome do Adotante: ${adotante.nome}
            E-mail do Adotante: ${adotante.email}
            Telefone do Adotante: ${adotante.contato}
            
            Próximos passos: 
            1. Entre em contato com ${adotante.nome}, para decidirem os detalhes e dar ou não continualidade à adoção.
            2. Recomendamos apenas confirmar a solicitação se ${adotante.nome} for um candidato(a) real para a adoção.
            3. O usuário interessado na adoção receberá um email com seu nome, email e contato cadastrados no Miaudote para entrar em contato. 
            
            ---------------------------------------------------------------
            QUERO CONFIRMAR A SOLICITAÇÃO: 
                
                Para CONFIRMAR a continuação da adoção de ${pet.nome}, clique no link abaixo:
                 
                LINK DE CONFIRMAÇÃO DA SOLICITAÇÃO DE ADOÇÃO: 
                $linkConfirmacaoSolicitacao
                
            ----------------------------------------------------------------
            
            QUERO CANCELAR A SOLICITAÇÃO DE ADOÇÃO RECEBIDA: 
                Caso não queira proceder com a adoção de ${pet.nome} clique no link abaixo:
                
                Link para CANCELAR a solicitação de adoção recebida: 
                $linkCancelaSolicitacao
            
            Não reponda este email.
            """.trimIndent()

        enviarEmail(
            to = responsavel.email,
            subject = titulo,
            conteudo = conteudo,
        )
    }
}