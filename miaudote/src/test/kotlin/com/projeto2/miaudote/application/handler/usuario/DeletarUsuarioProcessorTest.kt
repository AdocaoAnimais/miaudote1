package com.projeto2.miaudote.application.handler.usuario

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.domain.enums.Castrado
import com.projeto2.miaudote.domain.enums.Porte
import com.projeto2.miaudote.domain.enums.Sexo
import com.projeto2.miaudote.domain.enums.Tipo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.net.URI
import java.time.Instant

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeletarUsuarioProcessorTest {

    private val usuarioService: UsuarioService = Mockito.mock<UsuarioService>()
    private val petService: PetService = Mockito.mock<PetService>()

    @Autowired
    lateinit var processor: ProcessorHandler<DeletarUsuarioHandler>

    lateinit var processorWithMock: ProcessorHandler<DeletarUsuarioHandler>

    @BeforeEach
    fun setUp() {
        this.processorWithMock = DeletarUsuarioProcessor(usuarioService, petService)
        this.processorWithMock
    }
    @AfterEach
    fun tearDown() {
        // Restaurar os mocks para o estado inicial
        Mockito.reset(usuarioService, petService)
    }
    @Test
    @DisplayName("Deletar usuário com sucesso")
    fun `deletar_usuario_com_sucesso`() {
        val usuarioId = 1L
        val pets = listOf(
            Pet(
                nome = "",
                castrado = Castrado.N,
                sexo = Sexo.F,
                descricao = "",
                idade = 1,
                imageData = null,
                id = null,
                idUsuario = 12L,
                porte = Porte.P,
                tipo = Tipo.C
            )
        )

        // Mock para retorno do usuário e pets
        Mockito.`when`(usuarioService.obterPorId(usuarioId)).thenReturn(usuarioExistenteMock())
        Mockito.`when`(petService.obterPetsUsuario(usuarioId)).thenReturn(pets)

        val token = createToken(usuarioId)
        val handler = DeletarUsuarioHandler.newOrProblem(token).getOrElse { return }
        val result = processorWithMock.process(handler)

        // Verificar se o serviço foi chamado corretamente
        Mockito.verify(usuarioService, Mockito.times(1)).deletar(usuarioId)
        Mockito.verify(petService, Mockito.times(1)).deletarTodos(pets)

        assertTrue(result.isSuccess)
        assertEquals(usuarioId, result.getOrNull())
    }

    @Test
    @DisplayName("Usuário não encontrado")
    fun `usuario_nao_encontrado`() {
        val usuarioId = 9999999999L

        // Mock para simular usuário não existente
        val token = createToken(usuarioId)
        val handler = DeletarUsuarioHandler.newOrProblem(token).getOrElse { return }
        val result = processor.process(handler)
        // Validar o resultado de falha
        assertTrue(result.isFailure)
        val problem = result.exceptionOrNull() as Problem
        assertEquals(problem.detail, "O usuário com id informado não esta cadastrado")
        assertEquals(problem.title, "Usuário não encontrado")
        assertEquals(problem.type, URI("/obter-usuario-por-id"))
    }

    @Test
    @DisplayName("Deletar usuário sem pets")
    fun `deletar_usuario_sem_pets`() {
        val usuarioId = 1L

        // Mock de usuário existente sem pets
        Mockito.`when`(usuarioService.obterPorId(usuarioId)).thenReturn(usuarioExistenteMock())
        Mockito.`when`(petService.obterPetsUsuario(usuarioId)).thenReturn(emptyList())

        val token = createToken(usuarioId)
        val handler = DeletarUsuarioHandler.newOrProblem(token).getOrElse { return }
        val result = processorWithMock.process(handler)

        // Verificar interações corretas
        Mockito.verify(usuarioService, Mockito.times(1)).deletar(usuarioId)
        Mockito.verify(petService, Mockito.times(1)).deletarTodos(emptyList())

        assertTrue(result.isSuccess)
        assertEquals(usuarioId, result.getOrNull())
    }

    private fun usuarioExistenteMock() = Usuario(
        id = 1L,
        nome = "João",
        sobrenome = "Silva",
        username = "joaosilva",
        email = "joao@teste.com",
        cpf = "12345678910",
        senha = "senha123",
        emailVerificado = false,
        contato = "",
        endereco = "",
        descricao = "",
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
