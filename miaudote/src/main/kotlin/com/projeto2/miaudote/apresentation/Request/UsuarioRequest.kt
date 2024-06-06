package com.projeto2.miaudote.apresentation.Request

import com.projeto2.miaudote.application.problems.Problem
import org.springframework.http.HttpStatus
import java.net.URI

class UsuarioCreate(
    val nome: String?,
    val sobrenome: String?,
    val username: String?,
    val senha: String?,
    val email: String?,
    val cpf: String?,
    val descricao: String?,
    val contato: String?,
    val endereco: String?,
) {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"

    fun validaEmailRegex (): Result<String?> {
        if (!this.email.isNullOrBlank() && this.email.matches(emailRegex.toRegex())) return Result.failure(
            criarUsuarioProblem(
                "Campo 'email' não é um email valido",
                "email",
                this.email
            )
        )
        return Result.success(email)
    }

    fun validaEmail(): Result<String> {
        if (this.email.isNullOrBlank()) return Result.failure(
            criarUsuarioProblem(
                "Campo 'email' não pode ser vazio",
                "email",
                this.email
            )
        )

        if (!this.email.matches(emailRegex.toRegex())) return Result.failure(
            criarUsuarioProblem(
                "Campo 'email' não é um email valido",
                "email",
                this.email
            )
        )

        return Result.success(email)
    }

    fun validaNome(): Result<String> {
        if (this.nome.isNullOrBlank()) return Result.failure(
            criarUsuarioProblem(
                "Campo 'nome' não pode ser vazio",
                "nome",
                this.nome
            )
        )

        return Result.success(nome)
    }

    fun validaSobrenome(): Result<String> {
        if (this.sobrenome.isNullOrBlank()) return Result.failure(
            criarUsuarioProblem(
                "Campo 'sobrenome' não pode ser vazio",
                "sobrenome",
                this.sobrenome
            )
        )

        return Result.success(sobrenome)
    }

    fun validaSenha(): Result<String> {
        if (this.senha.isNullOrBlank() || this.senha.length <= 5) {
            return Result.failure(
                criarUsuarioProblem(
                    "Campo 'senha' não pode ser null ou menor que cinco caracteres",
                    "senha",
                    this.senha
                )
            )
        }

        return Result.success(senha)
    }

    fun validaCpf(): Result<String> {
        if (this.cpf.isNullOrBlank() || this.cpf.length != 11) {
            return Result.failure(
                criarUsuarioProblem(
                    "Campo 'cpf' precisa ter 11 caracteres",
                    "cpf",
                    this.cpf
                )
            )
        }

        return Result.success(cpf)
    }

    fun validaEndereco(): Result<String> {
        if (this.endereco.isNullOrBlank()) return Result.failure(
            criarUsuarioProblem(
                "Campo 'endereco' não pode ser vazio",
                "endereco",
                this.endereco
            )
        )

        return Result.success(endereco)
    }

    fun validaUsername(): Result<String> {
        if (this.username.isNullOrBlank()) return Result.failure(
            criarUsuarioProblem(
                "Campo 'username' não pode ser vazio",
                "username",
                this.username
            )
        )

        return Result.success(username)
    }

    private fun criarUsuarioProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
        title = "Não foi possivel criar um usuário",
        detail = detalhe,
        type = URI("/cadastrar-pet"),
        status = HttpStatus.BAD_REQUEST,
        extra = mapOf(campo to valor)
    )
}

class UsuarioUpdate(
    val nome: String?,
    val sobrenome: String?,
    val username: String?,
    val email: String?,
    val cpf: String?,
    val descricao: String?,
    val contato: String?,
    val endereco: String?,
    val senha: String?,
)