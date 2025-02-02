package com.projeto2.miaudote.domain.entities.usuario

import com.projeto2.miaudote.application.problems.Problem
import jakarta.persistence.*
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import java.net.URI
/**
 * Representa um usuário do sistema.
 *
 * Contém informações pessoais como nome, sobrenome, username, CPF, email, senha e dados adicionais
 * como endereço, contato, perfil de acesso e descrição.
 */
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

    @Column(name = "contato", unique = false)
    val contato: String?,

    @Column(name = "perfil_acesso")
    val perfilAcesso: String? = "A",

    @Column(name = "descricao")
    val descricao: String?,

    @Column(name = "email_verificado")
    val emailVerificado: Boolean = false,
) {
    /**
     * Valida se a senha fornecida corresponde à senha do usuário.
     *
     * @param senha A senha fornecida para validação.
     * @param passwordEncoder O objeto que realiza a validação da senha.
     * @return Retorna true se a senha for válida, caso contrário false.
     */
    fun validaLogin(senha: String, passwordEncoder: PasswordEncoder): Boolean {
        return passwordEncoder.matches(senha, this.senha)
    }

}
/**
 * Função de extensão para verificar se um usuário existe e retornar um problema caso não.
 *
 * @return Um `Result` contendo o usuário se encontrado ou um erro caso contrário.
 */
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