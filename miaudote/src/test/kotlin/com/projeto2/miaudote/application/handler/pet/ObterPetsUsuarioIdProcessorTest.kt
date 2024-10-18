package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.PetService
import com.projeto2.miaudote.application.services.SolicitacaoAdocaoService
import com.projeto2.miaudote.apresentation.Response.PetPost
import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.domain.enums.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.net.URI
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ObterPetsUsuarioIdProcessorTest {

    private val petService: PetService = Mockito.mock(PetService::class.java)
    private val solicitacaoService: SolicitacaoAdocaoService = Mockito.mock(SolicitacaoAdocaoService::class.java)

    lateinit var processor: ProcessorHandler<ObterPetsUsuarioIdHandler>

    @BeforeEach
    fun setUp() {
        processor = ObterPetsUsuarioIdProcessor(petService, solicitacaoService)
    }

    @Test
    @DisplayName("Falha ao criar handler com ID inválido")
    fun `id_usuario_invalido`() {
        val invalidToken = createToken("invalid-id")

        val result = ObterPetsUsuarioIdHandler.newOrProblem(invalidToken)

        assertTrue(result.isFailure)
        val problem = result.exceptionOrNull() as Problem
        assertEquals(problem.detail, "Id do usuário inválido.")
        assertEquals(problem.title, "Não foi possivel obter o pet")
        assertEquals(problem.type, URI("/obter-pet-por-id"))
    }

    @Test
    @DisplayName("Criar handler com ID válido")
    fun `criar_handler_com_id_valido`() {
        val validToken = createToken(123L)

        val result = ObterPetsUsuarioIdHandler.newOrProblem(validToken)

        assertTrue(result.isSuccess)
        val handler = result.getOrNull()
        assertEquals(handler?.id, 123L)
    }

    @Test
    @DisplayName("Obter pets do usuário com sucesso")
    fun `obter_pets_usuario_sucesso`() {
        val validToken = createToken(123L)
        val handler = ObterPetsUsuarioIdHandler.newOrProblem(validToken).getOrElse { return }

        val petList = listOf(mockPet(1L, "Pet 1"), mockPet(2L, "Pet 2"))
        Mockito.`when`(petService.obterPetsUsuario(handler.id)).thenReturn(petList)

        val solicitacaoAdocaoList = listOf(mockSolicitacaoAdocao(1L, handler.id))
        Mockito.`when`(solicitacaoService.obterTodasPetId(any())).thenReturn(solicitacaoAdocaoList)

        val response = processor.process(handler).getOrElse { return }
        val petPosts = response as List<PetPost>

        assertEquals(petPosts.size, petList.size)
        assertEquals(petPosts[0].nome, "Pet 1")
        assertEquals(petPosts[1].nome, "Pet 2")
        assertEquals("Solicitações não respondidas", StatusResponsavel.gerarStatus(solicitacaoAdocaoList.size).nome)
    }

    // Funções auxiliares para mock de Pet e Solicitação de Adoção
    private fun mockPet(id: Long, nome: String): Pet {
        return Pet(
            id = id,
            nome = nome,
            descricao = "Descrição do $nome",
            porte = Porte.M,
            sexo = Sexo.M,
            tipo = Tipo.C,
            castrado = Castrado.C,
            imageData = null,
            dataCadastro = LocalDateTime.now(),
            idUsuario = 1L,
            idade = 1
        )
    }

    private fun mockSolicitacaoAdocao(petId: Long, usuarioId: Long): SolicitacaoAdocao {
        return SolicitacaoAdocao(
            id = UUID.randomUUID(), petId = petId, usuarioResponsavel = 2L, usuarioAdotante = 3L
        )
    }

    private fun createToken(userId: Long): JwtAuthenticationToken {
        val jwt = Jwt.withTokenValue("fake-token").header("alg", "HS256").subject(userId.toString())  // ID válido
            .issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(3600)).build()

        return JwtAuthenticationToken(jwt)
    }

    private fun createToken(invalidId: String): JwtAuthenticationToken {
        val jwt = Jwt.withTokenValue("fake-token").header("alg", "HS256").subject(invalidId)  // ID inválido
            .issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(3600)).build()

        return JwtAuthenticationToken(jwt)
    }
}
