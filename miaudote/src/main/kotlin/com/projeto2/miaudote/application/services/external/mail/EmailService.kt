package com.projeto2.miaudote.application.services.external.mail

import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.domain.entities.pet.Pet
import com.projeto2.miaudote.domain.entities.usuario.Usuario
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.mail.MailAuthenticationException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.net.URI

/**
 * Serviço responsável pelo envio de emails através do JavaMailSender.
 * Contém métodos para enviar diferentes tipos de emails, como confirmação de adoção e verificação de email.
 */
@Service
class EmailService(
    val mailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    private val from: String,
) {
    /**
     * Envia um email simples para o destinatário especificado.
     *
     * @param to O endereço de email do destinatário.
     * @param subject O assunto do email.
     * @param conteudo O conteúdo do email.
     * @return Um resultado indicando se o envio foi bem-sucedido ou não.
     */
    fun enviarEmail(to: String, subject: String, conteudo: String): Result<Any> {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = subject
        message.text = conteudo
        message.from = from
        try {
            mailSender.send(message)
        } catch (e: MailAuthenticationException) {
            return enviarEmailProblem(
                e.message ?: "Erro desconhecido enviar email.",
                "email"
            ).toFailure()
        }
        return Result.success("")
    }
    /**
     * Envia um email para o usuário adotante com informações sobre a adoção e links para confirmar ou cancelar a adoção.
     *
     * @param adotante O usuário adotante que receberá o email.
     * @param pet O pet sendo adotado.
     * @param linkConfirmaAdocao O link para confirmar a adoção.
     * @param linkCancelaAdocao O link para cancelar a adoção.
     * @param responsavel O responsável pelo pet.
     * @return Um resultado indicando se o envio foi bem-sucedido ou não.
     */
    fun enviarEmailUsuarioAdotante(
        adotante: Usuario,
        pet: Pet,
        linkConfirmaAdocao: String,
        linkCancelaAdocao: String,
        responsavel: Usuario
    ): Result<Any> {
        val titulo = "[MIAUDOTE] Confirmação da Adoção - ${pet.nome}"
        val conteudo = """Prezado ${adotante.nome} ${adotante.sobrenome},
            O tutor responsável quer dar continualidade à adoção do(a) ${pet.nome}.
            
            Segue abaixo o contato do tutor responsável pelo animal, você deve entrar em contato para dar continualidade à adoção:

            Nome do Tutor: ${responsavel.nome} ${responsavel.sobrenome}
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

        return enviarEmail(
            to = adotante.email,
            subject = titulo,
            conteudo = conteudo,
        )
    }
    /**
     * Envia um email para o responsável com informações sobre a solicitação de adoção de um pet e links para confirmar ou cancelar a solicitação.
     *
     * @param responsavel O responsável pelo pet que receberá o email.
     * @param pet O pet sendo solicitado para adoção.
     * @param linkConfirmacaoSolicitacao O link para confirmar a solicitação de adoção.
     * @param linkCancelaSolicitacao O link para cancelar a solicitação de adoção.
     * @param adotante O usuário adotante que fez a solicitação.
     * @return Um resultado indicando se o envio foi bem-sucedido ou não.
     */
    fun enviarEmailUsuarioResponsavel(
        responsavel: Usuario,
        pet: Pet,
        linkConfirmacaoSolicitacao: String,
        linkCancelaSolicitacao: String,
        adotante: Usuario
    ): Result<Any> {
        val titulo = "[MIAUDOTE] Solicitação de Adoção - ${pet.nome}"
        val conteudo = """
            Prezado ${responsavel.nome} ${responsavel.sobrenome},
            recebimento uma solicitação de adoção para adotar o(a) ${pet.nome}.
            
            Segue abaixo o contato do usuario interessado em adotar o(a) ${pet.nome}.
            Você deve entrar em contato para dar continualidade à adoção:
  
            Nome do Adotante: ${adotante.nome} ${adotante.sobrenome}
            E-mail do Adotante: ${adotante.email}
            Telefone do Adotante: ${adotante.contato}
            
            Próximos passos: 
            1. Entre em contato com ${adotante.nome}, para decidirem os detalhes e dar ou não continualidade à adoção.
            2. Recomendamos apenas confirmar a solicitação se ${adotante.nome} for um candidato(a) real para a adoção.
            3. Ao CONFIRMAR a solicitação o usuário interessado na adoção receberá um email com seu nome, email e contato cadastrados no Miaudote para entrar em contato. 
            
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

        return enviarEmail(
            to = responsavel.email,
            subject = titulo,
            conteudo = conteudo,
        )
    }
    /**
     * Envia um email para o usuário com um link para confirmar seu endereço de email.
     *
     * @param usuario O usuário que precisa confirmar seu email.
     * @param linkVerificacao O link para confirmar o email.
     * @return Um resultado indicando se o envio foi bem-sucedido ou não.
     */
    fun enviarEmailVerificacao(
        usuario: Usuario,
        linkVerificacao: String,
    ): Result<Any> {
        val titulo = "[MIAUDOTE] Confirme seu email"
        val conteudo = """
            Prezado ${usuario.nome} ${usuario.sobrenome},
            
            Obrigado por se cadastrar no Miaudote!
            
            Precisamos de um pouco mais de informações para concluir seu cadastro, incluindo a confirmação do seu e-mail.

            CLIQUE ABAIXO PARA CONFIRMAR SEU EMAIL:

            $linkVerificacao

            Se tiver problemas, por favor, cole o URL acima no seu navegador.
            
            Não responda este email.
            """.trimIndent()

        return enviarEmail(
            to = usuario.email,
            subject = titulo,
            conteudo = conteudo,
        )
    }
}

/**
 * Gera um objeto Problem com informações sobre falhas no envio de email.
 *
 * @param detalhe A descrição do erro ocorrido ao tentar enviar o email.
 * @param campo O campo relacionado ao erro (geralmente "email").
 * @param valor O valor associado ao erro (se aplicável).
 * @return Um objeto Problem com os detalhes do erro.
 */
private fun enviarEmailProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel enviar o email",
    detail = detalhe,
    type = URI("/email-service"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)