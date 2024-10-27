package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.BaseTestConfig
import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.application.services.ValidacaoEmailService
import com.projeto2.miaudote.domain.entities.Usuario
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.net.URI
import java.time.Instant

class MandarVerificacaoEmailProcessorTest : BaseTestConfig() {

    private val validacaoEmailService: ValidacaoEmailService = Mockito.mock<ValidacaoEmailService>()
    private val usuarioService: UsuarioService = Mockito.mock<UsuarioService>()

    @Autowired
    lateinit var processor: ProcessorHandler<MandarVerificacaoEmailHandler>

    lateinit var processorWithMock: ProcessorHandler<MandarVerificacaoEmailHandler>

    @BeforeEach
    fun setUp() {
        this.processorWithMock = MandarVerificacaoEmailProcessor(validacaoEmailService, usuarioService)
    }

    @AfterEach
    fun tearDown() {
        // Restaurar os mocks para o estado inicial após cada teste
        Mockito.reset(validacaoEmailService, usuarioService)
    }

    @Test
    @DisplayName("Usuário não encontrado")
    fun `usuario_nao_encontrado`() {
        val usuarioId = 9999999L


        val token = createToken(usuarioId)
        val handler = MandarVerificacaoEmailHandler.newOrProblem(token).getOrElse { return }
        val result = processor.process(handler)

        // Verificar que o resultado é uma falha
        assertTrue(result.isFailure)
        val problem = result.exceptionOrNull() as Problem
        assertEquals(problem.detail, "Usuário não encontrado.")
        assertEquals(problem.title, "Não foi possivel mandar o email de validacao")
        assertEquals(problem.type, URI("/verificar-email"))
    }

    @Test
    @DisplayName("Email já verificado")
    fun `email_ja_verificado`() {
        val usuarioId = 1L
        val usuario = usuarioExistenteMock(emailVerificado = true)

        // Mock para simular que o usuário já verificou o email
        Mockito.`when`(usuarioService.obterPorId(usuarioId)).thenReturn(usuario)

        val token = createToken(usuarioId)
        val handler = MandarVerificacaoEmailHandler.newOrProblem(token).getOrElse { return }
        val result = processorWithMock.process(handler)

        // Verificar se o serviço de email não foi chamado
        Mockito.verify(validacaoEmailService, Mockito.never()).mandarEmailVerificacao(any())

        // Verificar que o resultado foi um sucesso com a mensagem "Email já verificado"
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), "Email já verificado")
    }

    @Test
    @DisplayName("Email enviado com sucesso")
    fun `email_enviado_com_sucesso`() {
        val usuarioId = 1L
        val usuario = usuarioExistenteMock(emailVerificado = false)

        // Mock para simular o retorno do usuário
        Mockito.`when`(usuarioService.obterPorId(usuarioId)).thenReturn(usuario)

        // Mock para simular o envio de email de verificação
        Mockito.`when`(validacaoEmailService.mandarEmailVerificacao(any())).thenReturn(Result.success(""))

        val token = createToken(usuarioId)
        val handler = MandarVerificacaoEmailHandler.newOrProblem(token).getOrElse { return }
        val result = processorWithMock.process(handler)

        // Verificar se o serviço de email foi chamado
        Mockito.verify(validacaoEmailService, Mockito.times(1)).mandarEmailVerificacao(usuario)

        // Verificar que o resultado foi um sucesso com a mensagem "Email mandado com sucesso!!!"
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), "Email mandado com sucesso!!!")
    }

    private fun usuarioExistenteMock(emailVerificado: Boolean = false) = Usuario(
        id = 1L,
        nome = "Nome",
        sobrenome = "Sobrenome",
        username = "user",
        email = "user@teste.com",
        cpf = "12345678900",
        descricao = "Descrição",
        contato = "999999999",
        endereco = "Endereço Teste",
        emailVerificado = emailVerificado,
        senha = "senha123"
    )

    private fun createToken(userId: Long): JwtAuthenticationToken {
        val jwt = Jwt.withTokenValue("fake-token")
            .header("alg", "HS256")
            .subject(userId.toString())
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build()

        return JwtAuthenticationToken(jwt)
    }
}