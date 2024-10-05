package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.JwtService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.application.services.ValidacaoEmailService
import com.projeto2.miaudote.apresentation.Request.UsuarioCreate
import com.projeto2.miaudote.apresentation.Response.LoginResponse
import com.projeto2.miaudote.domain.entities.Usuario
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import java.net.URI

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CriarUsuarioProcessorTest {

    @Autowired
    lateinit var processor: ProcessorHandler<CriarUsuarioHandler>
    @Autowired
    lateinit var jwtService: JwtService

    private val service: UsuarioService = Mockito.mock<UsuarioService>()
    private val validacaoEmailService: ValidacaoEmailService = Mockito.mock<ValidacaoEmailService>()
    lateinit var processorWithMock: ProcessorHandler<CriarUsuarioHandler>

    @BeforeEach
    fun setUp(){
        this.processorWithMock = CriarUsuarioProcessor(service, jwtService, validacaoEmailService)
    }
    @Test
    @DisplayName("Usuário já existe pelo email")
    fun `usuario_ja_existe_email`() {
        val usuario = UsuarioCreate(
            nome = "Teste",
            sobrenome = "Teste",
            username = "testuser",
            senha = "senha123",
            email = "patolino@pato.com",
            cpf = "12345678900",
            descricao = "Teste",
            contato = "99912345678",
            endereco = "98400000"
        )
        val result = CriarUsuarioHandler.newOrProblem(usuario)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Usuário com o email 'patolino@pato.com' já existe")
            assertEquals(problem.title, "Não foi possivel criar um usuário")
            assertEquals(problem.type, URI("/cadastrar-usuario"))
        }
    }

    @Test
    @DisplayName("Usuário já existe pelo CPF")
    fun `usuario_ja_existe_cpf`() {
        val usuario = UsuarioCreate(
            nome = "Teste",
            sobrenome = "Teste",
            username = "testuser2",
            senha = "senha123",
            email = "outroemail@teste.com",
            cpf = "12345678910",
            descricao = "Teste",
            contato = "99912345678",
            endereco = "98400000"
        )
        val result = CriarUsuarioHandler.newOrProblem(usuario)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Usuário com o cpf '12345678910' já existe")
            assertEquals(problem.title, "Não foi possivel criar um usuário")
            assertEquals(problem.type, URI("/cadastrar-usuario"))
        }
    }

    @Test
    @DisplayName("Usuário já existe pelo username")
    fun `usuario_ja_existe_username`() {
        val usuario = UsuarioCreate(
            nome = "Teste",
            sobrenome = "Teste",
            username = "patolino",
            senha = "senha123",
            email = "diferente@teste.com",
            cpf = "98765432100",
            descricao = "Teste",
            contato = "99912345678",
            endereco = "98400000"
        )
        val result = CriarUsuarioHandler.newOrProblem(usuario)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Usuário com o username 'patolino' já existe")
            assertEquals(problem.title, "Não foi possivel criar um usuário")
            assertEquals(problem.type, URI("/cadastrar-usuario"))
        }
    }

    @Test
    @DisplayName("Falha ao enviar email de verificação")
    fun `falha_ao_enviar_email`() {
        val usuario = UsuarioCreate(
            nome = "Teste",
            sobrenome = "Teste",
            username = "usuario_novo_453",
            senha = "senha123",
            email = "usuariotestes@teste.com",
            cpf = "96878785749",
            descricao = "Teste",
            contato = "1234565467",
            endereco = "98400000"
        )
        Mockito.`when`(service.obterPorEmail(any())).thenReturn(null)
        Mockito.`when`(service.obterPorCpf(any())).thenReturn(null)
        Mockito.`when`(service.obterUsername(any())).thenReturn(null)
        Mockito.`when`(validacaoEmailService.mandarEmailVerificacao(any<Usuario>()))
            .thenReturn(Result.failure(
                Problem(
                title ="Erro ao enviar o email",
                    detail ="Erro de conexão",
                    type = URI("/email-service"),
                    status = HttpStatus.BAD_REQUEST,
                    extra = null
                )
            ))

        val result = CriarUsuarioHandler.newOrProblem(usuario)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processorWithMock.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Erro de conexão")
            assertEquals(problem.title, "Erro ao enviar o email")
            assertEquals(problem.type, URI("/email-service"))
        }
    }

    @Test
    @DisplayName("Usuário criado com sucesso")
    fun `usuario_criado_com_sucesso`() {
        val usuario = UsuarioCreate(
            nome = "Teste",
            sobrenome = "Teste",
            username = "usuario_novo_453",
            senha = "senha123",
            email = "usuariotestes@teste.com",
            cpf = "96878785749",
            descricao = "Teste",
            contato = "1234565467",
            endereco = "98400000"
        )
        val usuarioCriado = Usuario(
            id = 1L,
            nome = "Novo",
            sobrenome = "Sobrenome",
            username = "user",
            email = "user@teste.com",
            cpf = "12345678900",
            descricao = "Descrição",
            contato = "999999999",
            endereco = "Endereço Teste",
            emailVerificado = true,
            senha = "senhaNova"
        )
        Mockito.`when`(service.obterPorEmail(any())).thenReturn(null)
        Mockito.`when`(service.criar(any())).thenReturn(usuarioCriado)
        Mockito.`when`(service.obterPorCpf(any())).thenReturn(null)
        Mockito.`when`(service.obterUsername(any())).thenReturn(null)
        Mockito.`when`(validacaoEmailService.mandarEmailVerificacao(any<Usuario>()))
            .thenReturn(Result.success(""))

        val result = CriarUsuarioHandler.newOrProblem(usuario)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processorWithMock.process(request)
        assertEquals(response.isFailure, false)
        val loginResponse = response.getOrNull() as LoginResponse
        assertEquals(loginResponse.username, usuarioCriado.username)
    }
}