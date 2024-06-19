package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.domain.entities.ValidacaoEmail
import com.projeto2.miaudote.infraestructure.repositories.ValidacaoEmailRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

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