package com.projeto2.miaudote.apresentation.request.pet

import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.domain.enums.*
import org.springframework.http.HttpStatus
import java.net.URI

data class PetCreate(
    val nome: String?,
    val sexo: String?,
    val porte: String?,
    val idade: String?,
    val tipo: String?,
    val castrado: String?,
    val descricao: String?
) {
    val LIMITECHAR: Int = 60
    val LIMDESCRICAO: Int = 500


    fun validaNome(): Result<String> {
        if (this.nome.isNullOrBlank() || this.nome.length <= 2) return Result.failure(
            criarPetProblem(
                "Campo 'nome' não pode ser null ou menor que três caracteres",
                "nome",
                this.nome
            )
        )
        if (this.nome.length > LIMITECHAR) return Result.failure(
            criarPetProblem(
                "Campo 'nome' muito longo",
                "nome",
                this.nome
            )
        )

        if (!validaTextoRegex(this.nome)) return Result.failure(
            criarPetProblem(
                "Campo 'nome' não é um nome valido",
                "nome",
                this.nome
            )
        )

        return Result.success(nome)
    }

    fun validaSexo(): Result<Sexo> {
        if (this.sexo.isNullOrBlank()) return Result.failure(
            criarPetProblem(
                "Campo 'sexo' não pode ser null",
                "sexo",
                this.sexo
            )
        )
        return this.sexo.toSexo()
    }

    fun validaPorte(): Result<Porte> {
        if (this.porte.isNullOrBlank()) return Result.failure(
            criarPetProblem(
                "Campo 'porte' não pode ser null",
                "porte",
                this.porte
            )
        )
        return this.porte.toPorte()
    }

    fun validaIdade(): Result<Int> {
        if (this.idade.isNullOrBlank()) return Result.failure(
            criarPetProblem(
                "Campo 'idade' não pode ser null",
                "idade",
                this.idade
            )
        )
        if (this.idade.toInt() > 40) return Result.failure(
            criarPetProblem(
                "Nenhum cão ou gato vive tanto tempo.",
                "idade",
                this.idade
            )
        )
        if (this.idade.toInt() < 0) return Result.failure(
            criarPetProblem(
                "Idade não pode ser negativa.",
                "idade",
                this.idade
            )
        )
        if (!validaNumeroRegex(this.idade)) return Result.failure(
            criarPetProblem(
                "Campo 'idade' não é um numero valido",
                "idade",
                this.idade
            )
        )

        return Result.success(idade.toInt())
    }

    fun validaTipo(): Result<Tipo> {
        if (this.tipo.isNullOrBlank()) return Result.failure(
            criarPetProblem(
                "Campo 'tipo' não pode ser null",
                "tipo",
                this.tipo
            )
        )
        return this.tipo.toTipo()
    }

    fun validaCastrado(): Result<Castrado> {
        if (this.castrado.isNullOrBlank()) return Result.failure(
            criarPetProblem(
                "Campo 'castrado' não pode ser null",
                "castrado",
                this.castrado
            )
        )
        return this.castrado.toCastrado()
    }

    fun validaDescricao(): Result<String?> {
        if (this.descricao.isNullOrEmpty()) {
            return Result.success(descricao)
        }
        if (this.descricao.length > LIMDESCRICAO) return Result.failure(
            criarPetProblem(
                "Campo 'descricao' muito longo, precisa ter no maximo 1000 caracteres",
                "descricao",
                this.descricao
            )
        )
        return Result.success(descricao)
    }

    private fun validaTextoRegex(texto: String?): Boolean {
        val textoRegex = "^[A-Za-zÀ-ÖØ-öø-ÿ \'-]+\$"
        return !(!texto.isNullOrEmpty() && !texto.matches(textoRegex.toRegex()))
    }

    private fun validaNumeroRegex(texto: String?): Boolean {
        val numeroRegex = "^[0-9]+\$"
        return !(!texto.isNullOrEmpty() && !texto.matches(numeroRegex.toRegex()))
    }

    private fun criarPetProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
        title = "Não foi possivel criar o pet",
        detail = detalhe,
        type = URI("/cadastrar-pet"),
        status = HttpStatus.BAD_REQUEST,
        extra = mapOf(campo to valor)
    )

}