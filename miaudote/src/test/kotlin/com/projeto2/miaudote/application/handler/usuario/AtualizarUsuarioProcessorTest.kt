package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.apresentation.Request.UsuarioCreate
import com.projeto2.miaudote.apresentation.Response.UsuarioResponse
import com.projeto2.miaudote.application.services.JwtService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.application.services.ValidacaoEmailService
import com.projeto2.miaudote.domain.entities.Usuario
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.kotlin.any
import org.mockito.Mockito
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest 
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.net.URI
import java.time.Instant 

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AtualizarUsuarioProcessorTest {

    private val service: UsuarioService = Mockito.mock<UsuarioService>()
    @Autowired
    lateinit var jwtService: JwtService
    private val validacaoEmailService: ValidacaoEmailService = Mockito.mock<ValidacaoEmailService>()

    lateinit var processorWithMock: ProcessorHandler<AtualizarUsuarioHandler>

    @Autowired
    lateinit var processor: ProcessorHandler<AtualizarUsuarioHandler>

    @BeforeEach
    fun setUp(){
        this.processorWithMock = AtualizarUsuarioProcessor(service, jwtService, validacaoEmailService)
    }
    @Test
    @DisplayName("Usuário não existe pelo id")
    fun `usuario_nao_existe_id`() {
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
        val token = createToken(99999999999L)
        val result = AtualizarUsuarioHandler.newOrProblem(usuario, token)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Usuário com o id '99999999999' não existe")
            assertEquals(problem.title, "Não foi possível atualizar o usuário")
            assertEquals(problem.type, URI("/atualizar-usuario"))
        }
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
        val token = createToken(1L)
        val result = AtualizarUsuarioHandler.newOrProblem(usuario, token)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Usuário com o email 'patolino@pato.com' já existe")
            assertEquals(problem.title, "Não foi possível atualizar o usuário")
            assertEquals(problem.type, URI("/atualizar-usuario"))
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
            cpf = "11223344556",
            descricao = "Teste",
            contato = "99912345678",
            endereco = "98400000"
        )
        val token = createToken(1L)
        val result = AtualizarUsuarioHandler.newOrProblem(usuario, token)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Usuário com o cpf '11223344556' já existe")
            assertEquals(problem.title, "Não foi possível atualizar o usuário")
            assertEquals(problem.type, URI("/atualizar-usuario"))
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
        val token = createToken(1L)
        val result = AtualizarUsuarioHandler.newOrProblem(usuario, token)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Usuário com o username 'patolino' já existe")
            assertEquals(problem.title, "Não foi possível atualizar o usuário")
            assertEquals(problem.type, URI("/atualizar-usuario"))
        }
    }

    @Test
    @DisplayName("Falha ao enviar email de verificação")
    fun `falha_ao_enviar_email`() {
        val usuarioExistente = Usuario(
            id = 1L,
            nome = "Nome",
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

        Mockito.`when`(service.obterPorId(eq(1L))).thenReturn(usuarioExistente)
        Mockito.`when`(validacaoEmailService.mandarEmailVerificacao(any<Usuario>()))
            .thenReturn(Result.failure(RuntimeException("Erro ao enviar email")))

        val token = createToken(1L)
        val usuario = UsuarioCreate(
            nome = "Novo",
            sobrenome = "Usuário",
            username = "novousuario",
            senha = "senhaSegura123",
            email = "novo1@teste.com",
            cpf = "11111111111",
            descricao = "Novo usuario para testes",
            contato = "1234567892",
            endereco = "98400000"
        )
        val handler = AtualizarUsuarioHandler.newOrProblem(usuario, token).getOrThrow()
        val result = processorWithMock.process(handler)
        val problem = result.exceptionOrNull() as Problem

        assertEquals(problem.detail, "Não foi possível enviar o email de verificação")
        assertEquals(problem.title, "Não foi possível atualizar o usuário")
        assertEquals(problem.type, URI("/atualizar-usuario"))
    }

    @Test
    @DisplayName("Atualização de usuário com sucesso")
    fun `atualizacao_com_sucesso`() {
        val usuarioExistente = Usuario(
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
        Mockito.`when`(service.obterPorId(any())).thenReturn(usuarioExistente)
        Mockito.`when`(validacaoEmailService.mandarEmailVerificacao(any<Usuario>())).thenReturn(Result.success(""))
        Mockito.`when`(service.atualizar(any<Usuario>())).thenReturn(usuarioExistente)
        val usuario = UsuarioCreate(
            nome = "Novo",
            sobrenome = "Usuário",
            username = "novousuario",
            senha = "senhaSegura123",
            email = "novo@teste.com",
            cpf = "11111111111",
            descricao = "Novo usuario para testes",
            contato = "1234567892",
            endereco = "98400000"
        )
        val token = createToken(1L)
        val handler = AtualizarUsuarioHandler.newOrProblem(usuario, token).getOrThrow()

        val result = processorWithMock.process(handler)
        assertTrue(result.isSuccess)

        val response = result.getOrNull() as UsuarioResponse
        assertEquals(response.nome, "Novo")
        assertEquals(response.email, "user@teste.com")
        assertEquals(response.username, "user")
    }
}

private fun createToken(userId: Long): JwtAuthenticationToken {
    val jwt = Jwt.withTokenValue("fake-token")
        .header("alg", "HS256")
        .subject(userId.toString())
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(3600))
        .build()

    // atualizar JwtAuthenticationToken
    return JwtAuthenticationToken(jwt)
}