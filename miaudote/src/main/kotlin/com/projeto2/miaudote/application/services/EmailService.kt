package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.domain.entities.Usuario
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    val mailSender: JavaMailSender,
) {
    fun enviarEmail(to: String, subject: String, conteudo: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = subject
        message.text = conteudo

        mailSender.send(message)
    }

    fun enviarEmailUsuarioSolicitante(solicitante: Usuario, pet: Pet, linkConfirmaAdocao: String, responsavel: Usuario) {
        val titulo = "[MIAUDOTE] Confirmação da Adoção - ${pet.nome}"
        val conteudo = """Prezado ${solicitante.nome} ${solicitante.sobrenome},
            Confirmamos o recebimento de sua solicitação de adoção para adotar o(a) ${pet.nome}.
            
            Segue abaixo o contato do tutor responsável pelo animal, você deve entrar em contato para dar continualidade à adoção:

            Nome do Tutor: ${responsavel.nome}
            E-mail do Tutor: ${responsavel.email}
            Telefone do Tutor: ${responsavel.contato}
            
            Para CONFIRMAR o processo de adoção de ${pet.nome}, clique no link abaixo:
    <strong>Apenas clique no link abaixo se a adoção correu com sucesso e o(a) ${pet.nome} já estiver na sua nova casa!!</strong>
            
            Confirmar adoção: $linkConfirmaAdocao
            
            Caso a adoção não tenha ocorrido, por favor ignore este email. 
            """.trimIndent()

        enviarEmail(
            to = solicitante.email,
            subject = titulo,
            conteudo = conteudo,
        )
    }

    fun enviarEmailUsuarioResponsavel(responsavel: Usuario, pet: Pet, linkConfirmacaoSolicitacao: String, adotante: Usuario) {
        val titulo = "[MIAUDOTE] Solicitação de Adoção - ${pet.nome}"
        val conteudo = """Prezado ${responsavel.nome} ${responsavel.sobrenome},
            Confirmamos o recebimento de sua solicitação de adoção para adotar o(a) ${pet.nome}.
            
    <strong>Segue abaixo o contato do usuario interessado em adotar o(a) ${pet.nome}.
            Você deve entrar em contato para dar continualidade à adoção:</strong> 
  
            Nome do Adotante: ${adotante.nome}
            E-mail do Adotante: ${adotante.email}
            Telefone do Adotante: ${adotante.contato}
            
            Para CONFIRMAR o a continuação da adoção de ${pet.nome}, clique no link abaixo:
    <strong>Apenas clique no link abaixo se o ${pet.nome} estiver diponível para adoção.</strong>
            Clicando no link abaixo, o usuário que solicitou a adoção receberá um email com seu nome, email e contato cadastrados no Miaudote, para entrar em contato. 
            
            LINK DE CONFIRMAÇÃO DA SOLICITAÇÃO DE ADOÇÃO: $linkConfirmacaoSolicitacao
            
    <strong>Caso a adoção não tenha ocorrido, por favor ignore este email.</strong>
            Não reponda este email.
            """.trimIndent()

        enviarEmail(
            to = responsavel.email,
            subject = titulo,
            conteudo = conteudo,
        )
    }
}