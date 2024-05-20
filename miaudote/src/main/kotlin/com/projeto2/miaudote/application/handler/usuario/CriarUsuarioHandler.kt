package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.apresentation.Request.UsuarioCreate
import com.projeto2.miaudote.apresentation.Response.UsuarioCreateResponse
import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.JwtService
import com.projeto2.miaudote.application.services.UsuarioService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.URI

@Service
class CriarUsuarioProcessor(
    private val service: UsuarioService,
    private val jwtService: JwtService,
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
            endereco = null,
            id = null,
        )
        val usuarioCriado = service.criar(usuario = usuario)
        val token = jwtService.generateToken(usuarioCriado)
        val response = UsuarioCreateResponse(
            username = usuarioCriado.username,
            token = token,
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
) : RequestHandler {
    companion object {
        fun newOrProblem(
            usuario: UsuarioCreate
        ): Result<CriarUsuarioHandler> {
            val nomeIn = usuario.nome
            if(nomeIn.isNullOrBlank()) return Result.failure(
                criarUsuarioProblem(
                    "Campo 'nome' não pode ser vazio",
                    "nome",
                    usuario.nome
                )
            )
            val sobrenomeIn = usuario.sobrenome
            if(sobrenomeIn.isNullOrBlank()) return Result.failure(
                criarUsuarioProblem(
                    "Campo 'sobrenome' não pode ser vazio",
                    "sobrenome",
                    usuario.sobrenome
                )
            )
            val usernameIn = usuario.username
            if(usernameIn.isNullOrBlank()) return Result.failure(
                criarUsuarioProblem(
                    "Campo 'username' não pode ser vazio",
                    "username",
                    usuario.username
                )
            )
            val emailIn = usuario.email
            if(emailIn.isNullOrBlank()) return Result.failure(
                criarUsuarioProblem(
                    "Campo 'email' não pode ser vazio",
                    "email",
                    usuario.email
                )
            )

            val senhaIn = usuario.senha
            if (senhaIn.isNullOrBlank() || senhaIn.length <= 5) {
                return Result.failure(
                    criarUsuarioProblem(
                        "Campo 'senha' não pode ser null ou menor que cinco caracteres",
                        "senha",
                        usuario.senha
                    )
                )
            }

            val cpfIn = usuario.cpf
            if (cpfIn.isNullOrBlank() || cpfIn.length != 11) {
                return Result.failure(
                    criarUsuarioProblem(
                        "Campo 'cpf' precisa ter 11 caracteres",
                        "cpf",
                        usuario.cpf
                    )
                )
            }

            return Result.success(
                CriarUsuarioHandler(
                    nome = nomeIn,
                    sobrenome = sobrenomeIn,
                    username = usernameIn,
                    email = emailIn,
                    senha = senhaIn,
                    cpf = cpfIn,
                    descricao = usuario.descricao,
                    contato = usuario.contato
                )
            )
        }
    }
}

private fun criarUsuarioProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel criar um usuário",
    detail = detalhe,
    type = URI("/cadastrar-pet"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)