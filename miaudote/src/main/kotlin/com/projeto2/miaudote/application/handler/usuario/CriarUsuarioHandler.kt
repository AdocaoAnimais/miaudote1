package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.EmailService
import com.projeto2.miaudote.application.services.JwtService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.application.services.ValidacaoEmailService
import com.projeto2.miaudote.apresentation.Request.UsuarioCreate
import com.projeto2.miaudote.apresentation.Response.LoginResponse
import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.domain.entities.ValidacaoEmail
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*

@Component
class CriarUsuarioProcessor(
    private val service: UsuarioService,
    private val jwtService: JwtService,
    private val validacaoEmailService: ValidacaoEmailService,

) : ProcessorHandler<CriarUsuarioHandler>() {

    override fun process(handler: CriarUsuarioHandler): Result<Any> {
        if (service.obterPorEmail(
                email = handler.email,
            ) != null
        ) return criarUsuarioProblem(
            "Usuário com o email '${handler.email}' já existe",
            "email",
            handler.email
        ).toFailure()

        if (service.obterPorCpf(
                cpf = handler.cpf
            ) != null
        ) return criarUsuarioProblem(
            "Usuário com o cpf '${handler.cpf}' já existe",
            "cpf",
            handler.cpf
        ).toFailure()

        if (service.obterUsername(username = handler.username) != null) return criarUsuarioProblem(
            "Usuário com o username '${handler.username}' já existe",
            "username",
            handler.username
        ).toFailure()
        val senha = jwtService.passwordEncoder.encode(handler.senha)
        val usuario = Usuario(
            nome = handler.nome,
            sobrenome = handler.sobrenome,
            username = handler.username,
            email = handler.email,
            senha = senha,
            cpf = handler.cpf,
            contato = handler.contato,
            descricao = handler.descricao,
            endereco = handler.endereco,
            id = null,
        )

        /* Mandar email de verificação */
        validacaoEmailService.mandarEmailVerificacao(usuario).getOrElse { return Result.failure(it) }
        /////////////////////////////////

        val usuarioCriado = service.criar(usuario = usuario)
        val token = jwtService.generateToken(usuarioCriado)
        val response = LoginResponse(
            username = usuarioCriado.username,
            accessToken = token,
            expiresIn = 5000L
        )
        return Result.success(response)

    }
}

class CriarUsuarioHandler private constructor(
    val nome: String,
    val sobrenome: String,
    val username: String,
    val senha: String,
    val email: String,
    val cpf: String,
    val descricao: String?,
    val contato: String?,
    val endereco: String?,
) : RequestHandler {
    companion object {
        fun newOrProblem(
            usuario: UsuarioCreate
        ): Result<CriarUsuarioHandler> {

            val nomeIn = usuario.validaNome().getOrElse { return Result.failure(it) }
            val sobrenomeIn = usuario.validaSobrenome().getOrElse { return Result.failure(it) }
            val usernameIn = usuario.validaUsername().getOrElse { return Result.failure(it) }
            val emailIn = usuario.validaEmail().getOrElse { return Result.failure(it) }
            val senhaIn = usuario.validaSenha().getOrElse { return Result.failure(it) }
            val cpfIn = usuario.validaCpf().getOrElse { return Result.failure(it) }
            val enderecoIn = usuario.validaEndereco().getOrElse { return Result.failure(it) }
            val contatoIn = usuario.validaContato().getOrElse { return Result.failure(it) }
            val descricao = usuario.validaDescricao().getOrElse { return Result.failure(it) }

            return Result.success(
                CriarUsuarioHandler(
                    nome = nomeIn,
                    sobrenome = sobrenomeIn,
                    username = usernameIn,
                    email = emailIn,
                    senha = senhaIn,
                    cpf = cpfIn,
                    descricao = descricao,
                    contato = contatoIn,
                    endereco = enderecoIn,
                )
            )
        }
    }
}

private fun criarUsuarioProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel criar um usuário",
    detail = detalhe,
    type = URI("/cadastrar-usuario"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)