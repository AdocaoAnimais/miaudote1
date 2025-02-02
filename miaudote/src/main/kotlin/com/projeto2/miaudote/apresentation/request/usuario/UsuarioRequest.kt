package com.projeto2.miaudote.apresentation.request.usuario

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
    /*
    Métodos de validação:


    fun validaNome(): Result<String>
        - !null/vazio, s/ caracter especial, <=60
    fun validaSobrenome(): Result<String>
        - !null/vazio, s/ caracter especial, <=60
    fun validaUsername(): Result<String>
        - !null/vazio, username regex, <= 50
        - "^[a-zA-Z][a-zA-Z0-9_-]+\$" = começa com letra, sem espaço, só numero, letra e '_' e '-'
    fun validaSenha(): Result<String>
        - !null/vazio, 6<=senha<=30
     fun validaEmail(): Result<String>
        - !null/vazio, email regex
        - "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$" = formato a_b.c@abc.efc
    fun validaCpf(): Result<String>
        - !null/vazio, 11 caracteres, numero regex (só numeros)
    fun validaDescricao(): Result<String?>
        - pode ser null/vazio, <= 1000 caracteres
    fun validaContato(): Result<String?>
        - pode ser null/vazio, 10 ou 11 digitos com DDD, numero regex (so numeros)
    fun validaEndereco(): Result<String?>
        - pode ser null/vazio, 8 digitos, numero regex (só numeros)
     */


    val LIMITECHAR : Int = 60
    val LIMDESCRICAO: Int = 500

    val textoRegex = "^[A-Za-zÀ-ÖØ-öø-ÿ \'-]+\$"
    val usernameRegex = "^[a-zA-Z][a-zA-Z0-9_-]+\$"
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    val numeroRegex = "^[0-9]+\$"

    fun validaNome(): Result<String> {
        if (this.nome.isNullOrEmpty()) return Result.failure(
            criarUsuarioProblem(
                "Campo 'nome' não pode ser vazio",
                "nome",
                this.nome
            )
        )
        if(this.nome.length > LIMITECHAR) return Result.failure(
            criarUsuarioProblem(
                "Campo 'nome' muito longo",
                "nome",
                this.nome
            )
        )
        if(!validaTextoRegex(this.nome)) return Result.failure(
            criarUsuarioProblem(
                "Campo 'nome' não é um nome valido",
                "nome",
                this.nome
            )
        )

        return Result.success(nome)
    }

    fun validaSobrenome(): Result<String> {
        if (this.sobrenome.isNullOrEmpty()) return Result.failure(
            criarUsuarioProblem(
                "Campo 'sobrenome' não pode ser vazio",
                "sobrenome",
                this.sobrenome
            )
        )
        if(this.sobrenome.length > LIMITECHAR) return Result.failure(
            criarUsuarioProblem(
                "Campo 'sobrenome' muito longo",
                "sobrenome",
                this.nome
            )
        )
        if(!validaTextoRegex(this.sobrenome)) return Result.failure(
            criarUsuarioProblem(
                "Campo 'sobrenome' não é um nome valido",
                "sobrenome",
                this.sobrenome
            )
        )

        return Result.success(sobrenome)
    }
    fun validaUsername(): Result<String> {
        if (this.username.isNullOrEmpty()) return Result.failure(
            criarUsuarioProblem(
                "Campo 'username' não pode ser vazio",
                "username",
                this.username
            )
        )
        if(!validaUsernameRegex(this.username)) return Result.failure(
            criarUsuarioProblem(
                "Campo 'username' não é um nome valido",
                "username",
                this.username
            )
        )
        if(this.username.length > 50) return Result.failure(
            criarUsuarioProblem(
                "Campo 'username' muito longo",
                "username",
                this.username
            )
        )

        return Result.success(username)
    }

    fun validaSenha(): Result<String> {
        if (this.senha.isNullOrEmpty() || this.senha.length <= 5 || this.senha.length > 30) {
            return Result.failure(
                criarUsuarioProblem(
                    "Campo 'senha' não pode ser null ou menor que seis caracteres ou maior que 30 caracteres",
                    "senha",
                    this.senha
                )
            )
        }

        return Result.success(senha)
    }

    fun validaEmail(): Result<String> {
        if (this.email.isNullOrEmpty()) return Result.failure(
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
    fun validaCpf(): Result<String> {

        if (this.cpf.isNullOrEmpty() || this.cpf.length != 11) {
            return Result.failure(
                criarUsuarioProblem(
                    "Campo 'cpf' precisa ter 11 caracteres",
                    "cpf",
                    this.cpf
                )
            )
        }
        if(!validaNumeroRegex(this.cpf)) return Result.failure(
            criarUsuarioProblem(
                "Campo 'cpf' não é um nome valido",
                "cpf",
                this.cpf
            )
        )

        return Result.success(cpf)
    }
    fun validaDescricao(): Result<String?>{
        if(this.descricao.isNullOrEmpty()){
            return Result.success(descricao)
        }
        if(this.descricao.length > LIMDESCRICAO) return Result.failure(
            criarUsuarioProblem(
                "Campo 'descricao' muito longo, precisa ter no maximo 1000 caracteres",
                "descricao",
                this.descricao
            )
        )
        return Result.success(descricao)
    }

    fun validaContato(): Result<String?> {
        if(this.contato.isNullOrEmpty()){
            return Result.success(contato)
        }
        if (this.contato.length !in 10..11) return Result.failure(
            criarUsuarioProblem(
                "Campo 'contato' precisa ter 10 ou 11 digitos, incluindo DDD",
                "endereco",
                this.contato
            )
        )
        if(!validaNumeroRegex(this.contato)) return Result.failure(
            criarUsuarioProblem(
                "Campo 'contato' não é um numero valido",
                "contato",
                this.contato
            )
        )

        return Result.success(contato)
    }

    fun validaEndereco(): Result<String?> {
        if(this.endereco.isNullOrEmpty()){
            return Result.success(endereco)
        }
        if (this.endereco.length != 8) return Result.failure(
            criarUsuarioProblem(
                "Campo 'endereco' precisa ter 8 digitos",
                "endereco",
                this.endereco
            )
        )
        if(!validaNumeroRegex(this.endereco)) return Result.failure(
            criarUsuarioProblem(
                "Campo 'endereco' não é um endereco valido",
                "endereco",
                this.endereco
            )
        )

        return Result.success(endereco)
    }

    private fun validaTextoRegex(texto: String?): Boolean {
        return !(!texto.isNullOrEmpty() && !texto.matches(textoRegex.toRegex()))
    }
    private fun validaNumeroRegex(texto: String?): Boolean {
        return !(!texto.isNullOrEmpty() && !texto.matches(numeroRegex.toRegex()))
    }
    private fun validaUsernameRegex(texto: String?): Boolean{
        return !(!texto.isNullOrEmpty() && !texto.matches(usernameRegex.toRegex()))
    }

    private fun criarUsuarioProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
        title = "Não foi possivel criar um usuário",
        detail = detalhe,
        type = URI("/cadastrar-pet"),
        status = HttpStatus.BAD_REQUEST,
        extra = mapOf(campo to valor)
    )

}