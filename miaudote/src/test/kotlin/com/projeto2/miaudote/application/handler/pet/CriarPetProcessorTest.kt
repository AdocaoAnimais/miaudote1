package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.services.UsuarioService
import com.projeto2.miaudote.apresentation.Request.PetCreate
import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.domain.enums.Castrado
import com.projeto2.miaudote.domain.enums.Porte
import com.projeto2.miaudote.domain.enums.Sexo
import com.projeto2.miaudote.domain.enums.Tipo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.net.URI
import java.time.Instant
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CriarPetProcessorTest {

    private val service: PetService = Mockito.mock(PetService::class.java)
    private val usuarioService: UsuarioService = Mockito.mock(UsuarioService::class.java)

    lateinit var processorWithMock: ProcessorHandler<CriarPetHandler>

    @Autowired
    lateinit var processor: ProcessorHandler<CriarPetHandler>

    @BeforeEach
    fun setUp() {
        this.processorWithMock = CriarPetProcessor(service, usuarioService)
    }

    @Test
    @DisplayName("Usuário inválido para cadastro de pet")
    fun `usuario_invalido_para_cadastro_de_pet`() {
        // Mockando o serviço para retornar null, simulando usuário inexistente
        Mockito.`when`(usuarioService.obterPorId(any())).thenReturn(null)

        val petCreate = mockValidPetCreate()
        val result = CriarPetHandler.newOrProblem(petCreate, createToken(123L))
        assertTrue(result.isSuccess)

        val request = result.getOrElse { return }
        val response = processorWithMock.process(request).exceptionOrNull()

        // Verifica se um problema adequado é retornado
        val problem = response as Problem
        assertEquals(problem.detail, "Usuario invalido para cadastro de pet")
        assertEquals(problem.title, "Não foi possivel criar o pet")
        assertEquals(problem.type, URI("/cadastrar-pet"))
    }

    @Test
    @DisplayName("Falha de verificação de email não verificado")
    fun `email_nao_verificado_para_cadastro_de_pet`() {
        // Mockando um usuário com email não verificado
        val usuario = mockUsuario(false)
        Mockito.`when`(usuarioService.obterPorId(any())).thenReturn(usuario)

        val petCreate = mockValidPetCreate()
        val result = CriarPetHandler.newOrProblem(petCreate, createToken(123L))
        assertTrue(result.isSuccess)

        val request = result.getOrElse { return }
        val response = processorWithMock.process(request).exceptionOrNull()

        // Verifica se o problema de email não verificado é retornado
        val problem = response as Problem
        assertEquals(
            problem.detail,
            "Email de cadastro não verificado, verifique seu email antes de cadastrar um animal!"
        )
        assertEquals(problem.title, "Não foi possivel criar o pet")
        assertEquals(problem.type, URI("/cadastrar-pet"))
    }

    @Test
    @DisplayName("Cadastro de pet com sucesso")
    fun `cadastro_de_pet_com_sucesso`() {
        // Mockando um usuário com email verificado e o pet sendo criado com sucesso
        val usuario = mockUsuario(true)
        val pet = mockPet()
        Mockito.`when`(usuarioService.obterPorId(any())).thenReturn(usuario)
        Mockito.`when`(service.criar(any())).thenReturn(pet)

        val petCreate = mockValidPetCreate()
        val result = CriarPetHandler.newOrProblem(petCreate, createToken(123L))
        assertTrue(result.isSuccess)

        val request = result.getOrElse { return }
        val response = processorWithMock.process(request).getOrNull()

        // Verifica se o pet foi cadastrado com sucesso
        assertEquals(response, pet)
    }

    @Test
    @DisplayName("Falha ao converter ID de usuário inválido")
    fun `id_usuario_invalido`() {
        val invalidToken = createToken("invalid-id")

        val petCreate = mockValidPetCreate()
        // Tentar criar um handler com um token inválido (ID não numérico)
        val result = CriarPetHandler.newOrProblem(petCreate, invalidToken)

        // Verifica se a falha é corretamente retornada com o problema adequado
        assertTrue(result.isFailure)
        val problem = result.exceptionOrNull() as Problem
        assertEquals(problem.detail, "Id do usuário inválido.")
        assertEquals(problem.title, "Não foi possivel criar o pet")
        assertEquals(problem.type, URI("/cadastrar-pet"))
    }

    @Test
    @DisplayName("Criação de handler com sucesso")
    fun `criacao_handler_com_sucesso`() {
        val validToken = createToken(123L)

        val petCreate = mockValidPetCreate()
        // Tentar criar um handler com dados válidos
        val result = CriarPetHandler.newOrProblem(petCreate, validToken)

        // Verifica se a criação do handler foi bem-sucedida
        assertTrue(result.isSuccess)

        val handler = result.getOrNull()!!
        assertEquals(handler.nome, petCreate.nome)
        assertEquals(handler.idUsuario, 123L)
    }

    // Funções auxiliares para mock de Pet e Usuario
    private fun mockUsuario(emailVerificado: Boolean): Usuario {
        return Usuario(
            id = 123L,
            nome = "Teste",
            sobrenome = "Usuario",
            username = "testeUsuario",
            email = "teste@usuario.com",
            cpf = "12345678900",
            descricao = "Descrição do usuário",
            contato = "999999999",
            endereco = "Rua Exemplo",
            emailVerificado = emailVerificado,
            senha = "senha"
        )
    }

    private fun mockPet(): Pet {
        return Pet(
            id = 1L,
            nome = "Pet Teste",
            idade = 2,
            porte = Porte.M,
            sexo = Sexo.M,
            tipo = Tipo.C,
            castrado = Castrado.C,
            descricao = "Descrição do pet",
            idUsuario = 123L,
            dataCadastro = LocalDateTime.now(),
            imageData = null,
        )
    }

    private fun mockValidPetCreate(): PetCreate {
        return PetCreate(
            nome = "Pet Teste",
            idade = "2",
            porte = "M",
            sexo = "M",
            tipo = "C",
            castrado = "C",
            descricao = "Descrição do pet"
        )
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

    private fun createToken(invalidId: String): JwtAuthenticationToken {
        val jwt = Jwt.withTokenValue("fake-token")
            .header("alg", "HS256")
            .subject(invalidId)  // ID inválido
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build()

        return JwtAuthenticationToken(jwt)
    }
}
