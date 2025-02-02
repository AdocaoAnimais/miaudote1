package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.problems.toFailure
import com.projeto2.miaudote.application.services.external.jwt.JwtService
import com.projeto2.miaudote.application.services.usuario.UsuarioService
import com.projeto2.miaudote.application.services.usuario.ValidacaoEmailService
import com.projeto2.miaudote.application.services.external.viacep.ViaCepService
import com.projeto2.miaudote.apresentation.request.usuario.UsuarioCreate
import com.projeto2.miaudote.apresentation.response.usuario.LoginResponse
import com.projeto2.miaudote.domain.entities.usuario.Usuario
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI
/**
 * Processor responsável por criar um novo usuário.
 *
 * Este processador valida as informações fornecidas para o novo usuário, verifica se o email, CPF e username são únicos,
 * valida o endereço através de um serviço externo (ViaCep), e envia um email de verificação. Após isso, cria o usuário e
 * gera um token de autenticação.
 *
 * @param service Serviço que gerencia os usuários.
 * @param jwtService Serviço responsável pela codificação e geração do token JWT.
 * @param validacaoEmailService Serviço que envia emails de verificação de email.
 * @param viaCepService Serviço que consulta dados de endereço via CEP.
 */
@Component
class CriarUsuarioProcessor(
    private val service: UsuarioService,
    private val jwtService: JwtService,
    private val validacaoEmailService: ValidacaoEmailService,
    private val viaCepService: ViaCepService,
) : ProcessorHandler<CriarUsuarioHandler>() {
    /**
     * Processa a criação de um novo usuário.
     *
     * Valida se o email, CPF e username são únicos. Se o endereço for fornecido, verifica a validade do CEP.
     * Em seguida, a senha é codificada e o usuário é criado. Um email de verificação é enviado, e o token de autenticação
     * é gerado e retornado na resposta.
     *
     * @param handler Dados necessários para criar um novo usuário.
     * @return Resultado da operação de criação, incluindo a resposta com o token de autenticação.
     */
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
        if (handler.endereco != null) {
            viaCepService.getDataFromCep(handler.endereco).getOrElse {
                return Result.failure(it)
            }
        }

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
/**
 * Classe de handler que contém os dados necessários para criar um novo usuário.
 *
 * Este handler é utilizado para validar e processar a solicitação de criação de um novo usuário, validando campos como nome, email e CPF.
 *
 * @param nome Nome do novo usuário.
 * @param sobrenome Sobrenome do novo usuário.
 * @param username Nome de usuário do novo usuário.
 * @param senha Senha do novo usuário.
 * @param email Email do novo usuário.
 * @param cpf CPF do novo usuário.
 * @param descricao Descrição adicional do novo usuário.
 * @param contato Contato adicional do novo usuário.
 * @param endereco Endereço do novo usuário.
 */
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
        /**
         * Cria um novo handler para criação de usuário ou retorna um erro caso os dados sejam inválidos.
         *
         * Este método valida todos os campos fornecidos para garantir que a criação do usuário seja feita corretamente.
         *
         * @param usuario Dados do novo usuário a ser criado.
         * @return Resultado contendo o handler para a criação ou erro de validação.
         */
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
/**
 * Cria um problema com detalhes sobre a falha ao tentar criar um usuário.
 *
 * Este método cria um problema que será retornado quando ocorrer algum erro ao tentar criar um novo usuário,
 * como a duplicação de email, CPF ou username.
 *
 * @param detalhe Descrição detalhada do erro ocorrido.
 * @param campo Campo que causou o erro.
 * @param valor Valor do campo que causou o erro.
 * @return O problema gerado com os detalhes do erro.
 */
private fun criarUsuarioProblem(detalhe: String, campo: String, valor: String? = "null") = Problem(
    title = "Não foi possivel criar um usuário",
    detail = detalhe,
    type = URI("/cadastrar-usuario"),
    status = HttpStatus.BAD_REQUEST,
    extra = mapOf(campo to valor)
)