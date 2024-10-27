package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.BaseTestConfig
import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.application.services.ValidacaoEmailService
import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.domain.entities.ValidacaoEmail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.net.URI
import java.time.Instant
import java.util.*

class ValidacaoEmailProcessorTest : BaseTestConfig() {

    private val service: ValidacaoEmailService = Mockito.mock(ValidacaoEmailService::class.java)
    private val usuarioService: UsuarioService = Mockito.mock(UsuarioService::class.java)

    lateinit var processorWithMock: ProcessorHandler<ValidacaoEmailHandler>

    @Autowired
    lateinit var processor: ProcessorHandler<ValidacaoEmailHandler>

    @BeforeEach
    fun setUp() {
        this.processorWithMock = ValidacaoEmailProcessor(service, usuarioService)
    }

    @Test
    @DisplayName("Verificação de email não encontrada")
    fun `verificacao_nao_encontrada`() {
        // Mockando o serviço para retornar null
        Mockito.`when`(service.obterPorId(any())).thenReturn(null)

        val id = UUID.randomUUID().toString()
        val result = ValidacaoEmailHandler.newOrProblem(id)
        assertTrue(result.isSuccess)

        val request = result.getOrElse { return }
        val response = processorWithMock.process(request).getOrNull()

        // Verifica se a mensagem de sucesso é retornada mesmo que a verificação não exista
        assertEquals(response, "Verificação não encontrada!!!")
    }

    @Test
    @DisplayName("Usuário não encontrado pelo username da validação de email")
    fun `usuario_nao_encontrado`() {
        // Mockando para simular validação existente e usuário inexistente
        val validacao = ValidacaoEmail(id = UUID.randomUUID(), username = "usuarioInexistente")
        Mockito.`when`(service.obterPorId(any())).thenReturn(validacao)
        Mockito.`when`(usuarioService.obterUsername(any())).thenReturn(null)

        val id = UUID.randomUUID().toString()
        val result = ValidacaoEmailHandler.newOrProblem(id)
        assertTrue(result.isSuccess)

        val request = result.getOrElse { return }
        val response = processorWithMock.process(request).exceptionOrNull()

        // Verifica se um problema adequado é retornado
        val problem = response as Problem
        assertEquals(problem.detail, "Usuário não encontrado.")
        assertEquals(problem.title, "Não foi possivel validar o email")
        assertEquals(problem.type, URI("/verificar-email"))
    }

    @Test
    @DisplayName("Email verificado com sucesso")
    fun `email_verificado_com_sucesso`() {
        // Mockando a validação e o usuário
        val validacao = ValidacaoEmail(id = UUID.randomUUID(), username = "usuarioTeste")
        val usuario = Usuario(
            id = 1L,
            nome = "Usuario",
            sobrenome = "Teste",
            username = "usuarioTeste",
            email = "usuario@teste.com",
            cpf = "12345678900",
            descricao = "Descrição",
            contato = "999999999",
            endereco = "Endereço Teste",
            emailVerificado = false,
            senha = "senhaNova"
        )

        Mockito.`when`(service.obterPorId(any())).thenReturn(validacao)
        Mockito.`when`(usuarioService.obterUsername(eq("usuarioTeste"))).thenReturn(usuario)

        Mockito.`when`(usuarioService.atualizar(any())).thenReturn(usuario.copy(emailVerificado = true))

        val id = UUID.randomUUID().toString()
        val result = ValidacaoEmailHandler.newOrProblem(id)
        assertTrue(result.isSuccess)

        val request = result.getOrElse { return }
        val response = processorWithMock.process(request).getOrNull()
        Mockito.verify(service, Mockito.times(1)).deletar(any())
        // Verifica se a mensagem de sucesso é retornada
        assertEquals(response, "Email verificado com sucesso!!!")
    }

    @Test
    @DisplayName("Falha ao converter ID de validação inválido")
    fun `id_validacao_invalido`() {
        val invalidVerificacaoId = "invalid-uuid"

        // Tentar criar um handler com um UUID inválido
        val result = ValidacaoEmailHandler.newOrProblem(invalidVerificacaoId)

        // Verifica se a falha é corretamente retornada com o problema adequado
        assertTrue(result.isFailure)
        val problem = result.exceptionOrNull() as Problem
        assertEquals(problem.detail, "Id da validacao do email inválida.")
        assertEquals(problem.title, "Não foi possivel validar o email")
        assertEquals(problem.type, URI("/verificar-email"))
    }

    @Test
    @DisplayName("Criação de handler com sucesso")
    fun `criacao_handler_com_sucesso`() {
        val validVerificacaoId = UUID.randomUUID().toString()

        // Tentar criar um handler com um UUID válido
        val result = ValidacaoEmailHandler.newOrProblem(validVerificacaoId)

        // Verifica se a criação do handler foi bem-sucedida
        assertTrue(result.isSuccess)

        val handler = result.getOrNull()!!
        assertEquals(handler.verificacaoId.toString(), validVerificacaoId)
    }

    private fun createToken(userId: Long): JwtAuthenticationToken {
        val jwt = Jwt.withTokenValue("fake-token")
            .header("alg", "HS256")
            .subject(userId.toString())  // ID válido
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build()

        return JwtAuthenticationToken(jwt)
    }
}
