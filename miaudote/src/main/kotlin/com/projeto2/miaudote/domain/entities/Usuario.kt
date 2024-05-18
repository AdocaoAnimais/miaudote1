package com.projeto2.miaudote.domain.entities

import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.apresentation.Request.LoginRequest
import jakarta.persistence.*
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import java.net.URI

@Entity
@Table(name = "usuario")
data class Usuario (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id", unique = true)
    val id: Long?,

    @Column(name = "nome", nullable = false)
    val nome: String,

    @Column(name = "sobrenome", nullable = false)
    val sobrenome: String,

    @Column(name = "username", unique = true, nullable = false)
    val username: String,

    @Column(name = "cpf", unique = true, nullable = false)
    val cpf: String,

    @Column(name = "email", unique = true, nullable = false)
    val email: String,

    @Column(name = "senha", nullable = false)
    val senha: String,

    @Column(name = "endereco")
    val endereco: String?,

    @Column(name = "contato", unique = true)
    val contato: String?,

    @Column(name = "perfil_acesso")
    val perfilAcesso: String? = "A",

    @Column(name = "descricao")
    val descricao: String?,

    @Column(name = "email_verificado")
    val emailVerificado: Boolean = false,
) {
    fun validaLogin(senha: String, passwordEncoder: PasswordEncoder): Boolean {
        return passwordEncoder.matches(senha, this.senha)
    }

}
fun Usuario?.toProblem(): Result<Usuario> {
    if(this != null) return Result.success(this)
    return Result.failure(
        Problem(
            title = "Usuário não encontrado",
            detail = "O usuário com id informado não esta cadastrado",
            type = URI("/obter-usuario-por-id"),
            status = HttpStatus.BAD_REQUEST,
            extra = null
        )
    )
}