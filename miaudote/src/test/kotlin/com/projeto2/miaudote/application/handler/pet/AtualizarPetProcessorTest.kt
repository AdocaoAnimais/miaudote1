package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.AdocaoService
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.apresentation.Request.PetCreate
import com.projeto2.miaudote.domain.entities.Adocao
import com.projeto2.miaudote.domain.entities.Pet
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
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AtualizarPetProcessorTest {

    private val petService: PetService = Mockito.mock(PetService::class.java)
    private val adocaoService: AdocaoService = Mockito.mock(AdocaoService::class.java)

    lateinit var processorWithMock: ProcessorHandler<AtualizarPetHandler>

    @Autowired
    lateinit var processor: ProcessorHandler<AtualizarPetHandler>

    @BeforeEach
    fun setUp() {
        this.processorWithMock = AtualizarPetProcessor(petService, adocaoService)
    }

    @Test
    @DisplayName("Falha ao converter ID de usuário inválido")
    fun `id_usuario_invalido`() {
        val invalidToken = createToken("invalid-id")

        val petCreate = mockValidPetCreate()
        val result = AtualizarPetHandler.newOrProblem(1L, petCreate, invalidToken)

        assertTrue(result.isFailure)
        val problem = result.exceptionOrNull() as Problem
        assertEquals(problem.detail, "Id do usuário não encontrado.")
        assertEquals(problem.title, "Não foi possível atualizar o pet")
        assertEquals(problem.type, URI("/atualizar"))
    }

    @Test
    @DisplayName("Pet não encontrado para atualização")
    fun `pet_nao_encontrado`() {
        val validToken = createToken(123L)

        Mockito.`when`(petService.obterPorId(any())).thenReturn(null)

        val petCreate = mockValidPetCreate()
        val result = AtualizarPetHandler.newOrProblem(1L, petCreate, validToken)
        assertTrue(result.isSuccess)

        val handler = result.getOrElse { return }
        val response = processorWithMock.process(handler).exceptionOrNull()

        val problem = response as Problem
        assertEquals(problem.detail, "Pet não encontrado")
        assertEquals(problem.title, "Não foi possível atualizar o pet")
        assertEquals(problem.type, URI("/atualizar"))
    }

    @Test
    @DisplayName("Pet já foi adotado e não pode ser atualizado")
    fun `pet_ja_adotado`() {
        val validToken = createToken(123L)

        val pet = mockPet()
        Mockito.`when`(petService.obterPorId(any())).thenReturn(pet)
        Mockito.`when`(adocaoService.obterPorPetId(any())).thenReturn(mockAdocao())

        val petCreate = mockValidPetCreate()
        val result = AtualizarPetHandler.newOrProblem(1L, petCreate, validToken)
        assertTrue(result.isSuccess)

        val handler = result.getOrElse { return }
        val response = processorWithMock.process(handler).exceptionOrNull()

        val problem = response as Problem
        assertEquals(problem.detail, "Pet que já foi adotado não pode ser editado.")
        assertEquals(problem.title, "Não foi possível atualizar o pet")
        assertEquals(problem.type, URI("/atualizar"))
    }

    @Test
    @DisplayName("Usuário sem permissão para atualizar pet")
    fun `usuario_sem_permissao_para_atualizar_pet`() {
        val validToken = createToken(123L)

        val pet = mockPet(usuarioId = 999L)  // Pet pertencente a outro usuário
        Mockito.`when`(petService.obterPorId(any())).thenReturn(pet)

        val petCreate = mockValidPetCreate()
        val result = AtualizarPetHandler.newOrProblem(1L, petCreate, validToken)
        assertTrue(result.isSuccess)

        val handler = result.getOrElse { return }
        val response = processorWithMock.process(handler).exceptionOrNull()

        val problem = response as Problem
        assertEquals(problem.detail, "Pet que já foi adotado não pode ser editado.")
        assertEquals(problem.title, "Não foi possível atualizar o pet")
        assertEquals(problem.type, URI("/atualizar"))
    }

    @Test
    @DisplayName("Atualização de pet com sucesso")
    fun `atualizacao_de_pet_com_sucesso`() {
        val validToken = createToken(123L)

        val pet = mockPet()
        Mockito.`when`(petService.obterPorId(any())).thenReturn(pet)
        Mockito.`when`(adocaoService.obterPorPetId(any())).thenReturn(null)  // Pet não foi adotado
        Mockito.`when`(petService.atualizar(any())).thenReturn(pet)

        val petCreate = mockValidPetCreate()
        val result = AtualizarPetHandler.newOrProblem(1L, petCreate, validToken)
        assertTrue(result.isSuccess)

        val handler = result.getOrElse { return }
        val response = processorWithMock.process(handler).getOrNull()

        assertEquals(response, pet)
    }

    @Test
    @DisplayName("Criação de handler com sucesso")
    fun `criacao_handler_com_sucesso`() {
        val validToken = createToken(123L)

        val petCreate = mockValidPetCreate()
        val result = AtualizarPetHandler.newOrProblem(1L, petCreate, validToken)

        assertTrue(result.isSuccess)

        val handler = result.getOrNull()!!
        assertEquals(handler.nome, petCreate.nome)
        assertEquals(handler.id, 1L)
    }

    // Funções auxiliares para mock de Pet, Adocao e Token
    private fun mockPet(usuarioId: Long = 123L): Pet {
        return Pet(
            id = 1L,
            nome = "Pet Teste",
            idade = 2,
            porte = Porte.M,
            sexo = Sexo.M,
            tipo = Tipo.C,
            castrado = Castrado.C,
            descricao = "Descrição do pet",
            idUsuario = usuarioId,
            dataCadastro = LocalDateTime.now(),
            imageData = null,
        )
    }

    private fun mockAdocao(): Adocao {
        return Adocao(
            id = 1L,
            petId = 1L,
            solicitacaoId = UUID.randomUUID(),
            dataAdocao = LocalDateTime.now()
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
