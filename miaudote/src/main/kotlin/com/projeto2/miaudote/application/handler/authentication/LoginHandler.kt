package com.projeto2.miaudote.application.handler.authentication

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.JwtService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.apresentation.Request.LoginRequest
import com.projeto2.miaudote.apresentation.Response.LoginResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI


/**
 * Processa a lógica de autenticação de login.
 *
 * @property service serviço para operações relacionadas a usuários.
 * @property serviceJwt serviço para geração e validação de tokens JWT.
 */
@Component
class LoginProcessor(
    private val service: UsuarioService,
    private val serviceJwt: JwtService
) : ProcessorHandler<LoginHandler>() {
    /**
     * Executa o processamento do login, validando credenciais e retornando um token JWT se bem-sucedido.
     *
     * @param handler contém os dados de login (username e senha).
     * @return resultado do processamento, podendo conter sucesso ou falha.
     */
    override fun process(handler: LoginHandler): Result<Any> {
        val usuario = service.obterUsername(handler.username) ?: return Result.failure(
            loginProblem(
                "Usuario com username '${handler.username}' não encontrado",
                "username",
                handler.username
            )
        )
        if (!usuario.validaLogin(handler.senha, serviceJwt.passwordEncoder)) return Result.failure(
            loginProblem(
                "A senha informada está incorreta",
                "senha",
                handler.senha
            )
        )
        val expiraEm = 300L
        val jwtToken = serviceJwt.generateToken(usuario)
        val response = LoginResponse(
            username = usuario.username,
            accessToken = jwtToken,
            expiresIn = expiraEm
        )

        return Result.success(response)
    }
}

class LoginHandler private constructor(
    val username: String,
    val senha: String
) : RequestHandler {
    companion object {
        /**
         * Valida e cria uma instância de LoginHandler, retornando problemas em caso de dados inválidos.
         *
         * @param login dados de login recebidos.
         * @return instância de LoginHandler ou falha com detalhes do problema.
         */
        fun newOrProblem(login: LoginRequest): Result<LoginHandler> {
            val username = login.username
            if (username.isNullOrBlank()) return Result.failure(
                loginProblem(
                    "Campo 'username' não pode ser null ou vazio",
                    "username",
                    login.username
                )
            )
            val senha = login.senha
            if (senha.isNullOrBlank()) return Result.failure(
                loginProblem(
                    "Campo 'senha' não pode ser null ou vazio",
                    "senha",
                    login.senha
                )
            )
            return Result.success(LoginHandler(username = username, senha = senha))
        }
    }
}
/**
 * Cria um objeto de problema específico para erros de login.
 *
 * @param detalhe mensagem detalhada do problema.
 * @param campo campo que causou o problema.
 * @param valor valor inválido fornecido, default é "null".
 * @return objeto de problema configurado.
 */
private fun loginProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel efetuar login",
    detail = detalhe,
    type = URI("/login"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)