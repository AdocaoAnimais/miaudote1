package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.BaseTestConfig
import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.apresentation.Response.UsuarioResponse
import com.projeto2.miaudote.domain.entities.Usuario
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.net.URI
import java.time.Instant

class ObterUsuarioProcessorTest : BaseTestConfig() {

    private val usuarioService: UsuarioService = Mockito.mock<UsuarioService>()

    @Autowired
    lateinit var processor: ProcessorHandler<ObterUsuarioHandler>

    lateinit var processorWithMock: ProcessorHandler<ObterUsuarioHandler>

    @BeforeEach
    fun setUp() {
        this.processorWithMock = ObterUsuarioProcessor(usuarioService)
    }

    @AfterEach
    fun tearDown() {
        // Restaurar os mocks para o estado inicial após cada teste
        Mockito.reset(usuarioService)
    }

    @Test
    @DisplayName("Usuário não encontrado")
    fun `usuario_nao_encontrado`() {
        val usuarioId = 99999999L

        val token = createToken(usuarioId)
        val handler = ObterUsuarioHandler.newOrProblem(token).getOrElse { return }
        val result = processor.process(handler)

        // Verificar que o resultado é uma falha
        assertTrue(result.isFailure)
        val problem = result.exceptionOrNull() as Problem
        assertEquals(problem.detail, "O usuário com id informado não esta cadastrado")
        assertEquals(problem.title, "Usuário não encontrado")
        assertEquals(problem.type, URI("/obter-usuario-por-id"))
    }

    @Test
    @DisplayName("Usuário obtido com sucesso")
    fun `usuario_obtido_com_sucesso`() {
        val usuarioId = 1L
        val usuario = usuarioExistenteMock()

        // Mock para simular o retorno do usuário
        Mockito.`when`(usuarioService.obterPorId(usuarioId)).thenReturn(usuario)

        val token = createToken(usuarioId)
        val handler = ObterUsuarioHandler.newOrProblem(token).getOrElse { return }
        val result = processorWithMock.process(handler)

        // Verificar se o resultado foi um sucesso
        assertTrue(result.isSuccess)

        val response = result.getOrNull() as UsuarioResponse
        assertEquals(response.id, usuario.id)
        assertEquals(response.nome, usuario.nome)
        assertEquals(response.email, usuario.email)
        assertEquals(response.username, usuario.username)
    }

    @Test
    @DisplayName("Falha ao criar handler devido a ID inválido no token")
    fun `id_invalido_no_token`() {
        // Criar um token JWT com nome inválido (não um número)
        val invalidToken = createTokenWithInvalidName()

        // Tentar criar um handler com o token inválido
        val result = ObterUsuarioHandler.newOrProblem(invalidToken)

        // Verificar que o resultado é uma falha e que o Problem contém as informações corretas
        assertTrue(result.isFailure)
        val problem = result.exceptionOrNull() as Problem
        assertEquals(problem.detail, "Id do usuário invalido.")
        assertEquals(problem.title, "Não foi possivel obter informações do usuario")
        assertEquals(problem.type, URI("/obter-usuario"))
    }

    @Test
    @DisplayName("Criação de handler com sucesso")
    fun `criacao_handler_com_sucesso`() {
        // Criar um token JWT válido com um ID numérico
        val validToken = createToken(1L)

        // Tentar criar um handler com o token válido
        val result = ObterUsuarioHandler.newOrProblem(validToken)

        // Verificar que o resultado foi bem-sucedido
        assertTrue(result.isSuccess)

        val handler = result.getOrNull()!!
        assertEquals(handler.id, 1L)
    }

    private fun createTokenWithInvalidName(): JwtAuthenticationToken {
        val jwt = Jwt.withTokenValue("fake-token")
            .header("alg", "HS256")
            .subject("invalid-id")  // ID inválido
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build()

        return JwtAuthenticationToken(jwt)
    }

    private fun usuarioExistenteMock() = Usuario(
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
