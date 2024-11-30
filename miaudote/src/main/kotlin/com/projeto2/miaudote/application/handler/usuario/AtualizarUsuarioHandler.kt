package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.JwtService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.application.services.ValidacaoEmailService
import com.projeto2.miaudote.application.services.ViaCepService
import com.projeto2.miaudote.apresentation.Request.UsuarioCreate
import com.projeto2.miaudote.apresentation.Response.UsuarioResponse
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI
/**
 * Processor responsável por atualizar as informações de um usuário.
 *
 * Este processador valida e atualiza as informações de um usuário, como nome, sobrenome, email, CPF, etc.
 * Além disso, ele envia um email de verificação caso o email do usuário seja alterado.
 *
 * @param service Serviço que gerencia os usuários.
 * @param jwtService Serviço responsável pela codificação e validação de senhas.
 * @param validacaoEmailService Serviço que envia emails de verificação de email.
 * @param viaCepService Serviço que consulta dados de endereço via CEP.
 */
@Component
class AtualizarUsuarioProcessor(
    private val service: UsuarioService,
    private val jwtService: JwtService,
    private val validacaoEmailService: ValidacaoEmailService,
    private val viaCepService: ViaCepService
) : ProcessorHandler<AtualizarUsuarioHandler>() {
    /**
     * Processa a atualização das informações de um usuário.
     *
     * Verifica se o usuário existe, se o email, CPF e username são únicos e se o endereço é válido.
     * Se o email for alterado, um email de verificação será enviado. Em seguida, o usuário é atualizado e a resposta é retornada.
     *
     * @param handler Dados necessários para atualizar o usuário.
     * @return Resultado da operação de atualização, incluindo a resposta com os dados do usuário atualizado.
     */
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
        if (handler.endereco != null) {
            viaCepService.getDataFromCep(handler.endereco).getOrElse {
                return Result.failure(it)
            }
        }
        val senhaAtualizada = if (!handler.senha.isNullOrEmpty()) jwtService.passwordEncoder.encode(handler.senha)
        else usuarioExistente.senha

        var usuarioAtualizado = usuarioExistente.copy(
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
        if (usuarioExistente.email != usuarioAtualizado.email) {
            usuarioAtualizado = usuarioAtualizado.copy(emailVerificado = false)
            validacaoEmailService.mandarEmailVerificacao(usuarioAtualizado).getOrElse {
                return atualizarUsuarioProblem(
                    "Não foi possível enviar o email de verificação",
                    "email",
                    handler.email
                ).toFailure()
            }
        }

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
            endereco = usuarioSalvo.endereco,
            email_verificado = usuarioSalvo.emailVerificado,
        )
        return Result.success(response)
    }
}
/**
 * Classe de handler que contém os dados necessários para atualizar um usuário.
 *
 * Este handler é utilizado para validar e processar a solicitação de atualização do usuário, validando campos como nome, email e CPF.
 *
 * @param id ID do usuário a ser atualizado.
 * @param nome Nome do usuário a ser atualizado.
 * @param sobrenome Sobrenome do usuário a ser atualizado.
 * @param username Nome de usuário a ser atualizado.
 * @param email Email do usuário a ser atualizado.
 * @param cpf CPF do usuário a ser atualizado.
 * @param descricao Descrição adicional do usuário.
 * @param contato Contato adicional do usuário.
 * @param endereco Endereço do usuário a ser atualizado.
 * @param senha Senha do usuário a ser atualizada.
 */
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
        /**
         * Cria um novo handler de atualização de usuário ou retorna um erro caso os dados sejam inválidos.
         *
         * Este método valida todos os campos fornecidos para garantir que a atualização do usuário seja feita corretamente.
         *
         * @param usuario Dados do usuário a ser atualizado.
         * @param token Token JWT contendo o ID do usuário.
         * @return Resultado contendo o handler para a atualização ou erro de validação.
         */
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
            val senhaIn = if (!usuario.senha.isNullOrEmpty()) usuario.validaSenha()
                .getOrElse { return Result.failure(it) } else null

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
/**
 * Cria um problema com detalhes sobre a falha ao tentar atualizar o usuário.
 *
 * Este método cria um problema que será retornado quando ocorrer algum erro ao tentar atualizar as informações de um usuário.
 *
 * @param detalhe Descrição detalhada do erro ocorrido.
 * @param campo Campo que causou o erro.
 * @param valor Valor do campo que causou o erro.
 * @return O problema gerado com os detalhes do erro.
 */
private fun atualizarUsuarioProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possível atualizar o usuário",
    detail = detalhe,
    type = URI("/atualizar-usuario"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)