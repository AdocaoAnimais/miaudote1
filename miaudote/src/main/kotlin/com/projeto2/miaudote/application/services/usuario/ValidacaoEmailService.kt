package com.projeto2.miaudote.application.services.usuario

import com.projeto2.miaudote.application.services.external.mail.EmailService
import com.projeto2.miaudote.domain.entities.usuario.Usuario
import com.projeto2.miaudote.domain.entities.usuario.ValidacaoEmail
import com.projeto2.miaudote.infraestructure.repositories.jpa.usuario.ValidacaoEmailRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull
/**
 * Serviço responsável por gerenciar a validação de email de usuários, incluindo a criação, exclusão e recuperação
 * de registros de validação de email, além do envio de emails de verificação para os usuários.
 *
 * @param repository O repositório responsável pela persistência de dados de validação de email.
 * @param emailService O serviço utilizado para enviar emails de verificação.
 * @param baseUrl A URL base usada para criar links de verificação de email.
 */
@Service
class ValidacaoEmailService(
    private val repository: ValidacaoEmailRepository,
    private val emailService: EmailService,
    @Value("\${base.url}")
    private val baseUrl: String,)
{
    fun criar(validacaoEmail: ValidacaoEmail): ValidacaoEmail {
        return repository.save(validacaoEmail)
    }
    fun deletar(id: UUID) {
        repository.deleteById(id)
    }
    fun obterPorId(id: UUID): ValidacaoEmail? {
        return repository.findById(id).getOrNull()
    }
    /**
     * Envia um email de verificação para o usuário, contendo um link único de validação.
     * O link é gerado com base no ID de validação de email recém-criado e enviado para o usuário.
     *
     * @param usuario O usuário para o qual o email de verificação será enviado.
     * @return O link de verificação gerado e enviado ao usuário.
     */
    fun mandarEmailVerificacao(usuario: Usuario): Result<Any> {
        val validacao = ValidacaoEmail(
            id = null,
            username = usuario.username
        )
        criar(validacao)
        val id = validacao.id.toString()
        val linkVerificacao =
            "$baseUrl/api/usuario/verificar-email/${id}"

        emailService.enviarEmailVerificacao(usuario, linkVerificacao)
        return Result.success(linkVerificacao)
    }
}