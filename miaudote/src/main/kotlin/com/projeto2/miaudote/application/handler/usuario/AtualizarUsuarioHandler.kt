package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.JwtService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.apresentation.Request.UsuarioCreate
import com.projeto2.miaudote.apresentation.Response.AtualizarUsuarioResponse
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.net.URI

@Service
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

        if (handler.email != null && usuarioExistente.email != handler.email) {
            if (service.obterPorEmail(email = handler.email) != null) {
                return atualizarUsuarioProblem(
                    "Usuário com o email '${handler.email}' já existe",
                    "email",
                    handler.email
                ).toFailure()
            }
        }

        if (handler.cpf != null && usuarioExistente.cpf != handler.cpf) {
            // Perform the uniqueness check only if the CPFs are different
            if (service.obterPorCpf(cpf = handler.cpf) != null) {
                return atualizarUsuarioProblem(
                    "Usuário com o cpf '${handler.cpf}' já existe",
                    "cpf",
                    handler.cpf
                ).toFailure()
            }
        }

        if (handler.username != null && usuarioExistente.username != handler.username) {
            if (service.obterUsername(username = handler.username) != null) {
                return atualizarUsuarioProblem(
                    "Usuário com o username '${handler.username}' já existe",
                    "username",
                    handler.username
                ).toFailure()
            }
        }

        val senhaAtualizada = handler.senha?.let { jwtService.passwordEncoder.encode(it) } ?: usuarioExistente.senha

        val usuarioAtualizado = usuarioExistente.copy(
            nome = handler.nome ?: usuarioExistente.nome,
            sobrenome = handler.sobrenome ?: usuarioExistente.sobrenome,
            username = handler.username ?: usuarioExistente.username,
            email = handler.email ?: usuarioExistente.email,
            senha = senhaAtualizada,
            cpf = handler.cpf ?: usuarioExistente.cpf,
            contato = handler.contato ?: usuarioExistente.contato,
            descricao = handler.descricao ?: usuarioExistente.descricao,
            endereco = usuarioExistente.endereco,
            id = usuarioExistente.id,
        )

        val usuarioSalvo = service.atualizar(usuario = usuarioAtualizado)
        val response = AtualizarUsuarioResponse(
            username = usuarioSalvo.username,
            id = usuarioSalvo.id,
            nome = usuarioSalvo.nome,
            sobrenome = usuarioSalvo.sobrenome,
            email = usuarioSalvo.email,
            cpf = usuarioSalvo.cpf,
            descricao = usuarioSalvo.descricao,
            contato = usuarioSalvo.contato
        )
        return Result.success(response)
    }
}
class AtualizarUsuarioHandler private constructor(
    val id: Long,
    val nome: String?,
    val sobrenome: String?,
    val username: String?,
    val senha: String?,
    val email: String?,
    val cpf: String?,
    val descricao: String?,
    val contato: String?,
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

            return Result.success(
                AtualizarUsuarioHandler(
                    id = id,
                    nome = usuario.nome,
                    sobrenome = usuario.sobrenome,
                    username = usuario.username,
                    senha = usuario.senha,
                    email = usuario.email,
                    cpf = usuario.cpf,
                    descricao = usuario.descricao,
                    contato = usuario.contato
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
