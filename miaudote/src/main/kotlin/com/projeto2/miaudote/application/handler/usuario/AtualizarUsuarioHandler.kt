package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.JwtService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.apresentation.Request.UsuarioCreate
import com.projeto2.miaudote.apresentation.Response.UsuarioResponse
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI

@Component
class AtualizarUsuarioProcessor(
    private val service: UsuarioService,
    private val jwtService: JwtService,
) : ProcessorHandler<AtualizarUsuarioHandler>() {

    override fun process(handler: AtualizarUsuarioHandler): Result<Any> {
        val usuarioExistente = service.obterPorId(handler.id)
            ?: return atualizarUsuarioProblem(
                "Usuário com o id '${handler.id}' não existe",
                "id",
                handler.id.toString()
            ).toFailure()

        if (usuarioExistente.email != handler.email) {
            if (service.obterPorEmail(email = handler.email) != null) {
                return atualizarUsuarioProblem(
                    "Usuário com o email '${handler.email}' já existe",
                    "email",
                    handler.email
                ).toFailure()
            }
        }

        if (usuarioExistente.cpf != handler.cpf) {
            // Perform the uniqueness check only if the CPFs are different
            if (service.obterPorCpf(cpf = handler.cpf) != null) {
                return atualizarUsuarioProblem(
                    "Usuário com o cpf '${handler.cpf}' já existe",
                    "cpf",
                    handler.cpf
                ).toFailure()
            }
        }

        if (usuarioExistente.username != handler.username && service.obterUsername(username = handler.username) != null) {
            return atualizarUsuarioProblem(
                "Usuário com o username '${handler.username}' já existe",
                "username",
                handler.username
            ).toFailure()
        }


        val senhaAtualizada = if (!handler.senha.isNullOrEmpty()) jwtService.passwordEncoder.encode(handler.senha)
        else usuarioExistente.senha

        val usuarioAtualizado = usuarioExistente.copy(
            nome = handler.nome,
            sobrenome = handler.sobrenome,
            username = handler.username,
            email = handler.email,
            cpf = handler.cpf,
            contato = handler.contato,
            descricao = handler.descricao,
            endereco = handler.endereco,
            senha = senhaAtualizada,
        )

        val usuarioSalvo = service.atualizar(usuario = usuarioAtualizado)
        val response = UsuarioResponse(
            username = usuarioSalvo.username,
            id = usuarioSalvo.id,
            nome = usuarioSalvo.nome,
            sobrenome = usuarioSalvo.sobrenome,
            email = usuarioSalvo.email,
            cpf = usuarioSalvo.cpf,
            descricao = usuarioSalvo.descricao,
            contato = usuarioSalvo.contato,
            endereco = usuarioSalvo.endereco
        )
        return Result.success(response)
    }
}

class AtualizarUsuarioHandler private constructor(
    val id: Long,
    val nome: String,
    val sobrenome: String,
    val username: String,
    val email: String,
    val cpf: String,
    val descricao: String?,
    val contato: String?,
    val endereco: String?,
    val senha: String?,
) : RequestHandler {
    companion object {
        fun newOrProblem(
            usuario: UsuarioCreate,
            token: JwtAuthenticationToken
        ): Result<AtualizarUsuarioHandler> {
            val id = token.name.toLongOrNull() ?: return Result.failure(
                atualizarUsuarioProblem(
                    "Id do usuário não encontrado.",
                    "ID",
                )
            )
            if (id <= 0) return Result.failure(
                atualizarUsuarioProblem(
                    "Campo 'id' não pode ser menor ou igual a zero",
                    "id",
                    id.toString()
                )
            )
            val nomeIn = usuario.validaNome().getOrElse { return Result.failure(it) }
            val sobrenomeIn = usuario.validaSobrenome().getOrElse { return Result.failure(it) }
            val usernameIn = usuario.validaUsername().getOrElse { return Result.failure(it) }
            val cpfIn = usuario.validaCpf().getOrElse { return Result.failure(it) }
            val emailIn = usuario.validaEmail().getOrElse { return Result.failure(it) }
            val enderecoIn = usuario.validaEndereco().getOrElse { return Result.failure(it) }
            val contatoIn = usuario.validaContato().getOrElse { return Result.failure(it) }
            val descricaoIn = usuario.validaDescricao().getOrElse { return Result.failure(it) }
            val senhaIn = usuario.validaSenha().getOrElse { return Result.failure(it) }


            return Result.success(
                AtualizarUsuarioHandler(
                    id = id,
                    nome = nomeIn,
                    sobrenome = sobrenomeIn,
                    username = usernameIn,
                    email = emailIn,
                    cpf = cpfIn,
                    descricao = descricaoIn,
                    contato = contatoIn,
                    endereco = enderecoIn,
                    senha = senhaIn,
                )
            )
        }
    }
}

private fun atualizarUsuarioProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possível atualizar o usuário",
    detail = detalhe,
    type = URI("/atualizar-usuario"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)