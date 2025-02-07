package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.BaseTestConfig
import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.services.pet.PetService
import com.projeto2.miaudote.application.services.adocao.SolicitacaoAdocaoService
import com.projeto2.miaudote.apresentation.response.pet.PetPost
import com.projeto2.miaudote.domain.entities.pet.Pet
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.domain.enums.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class ObterPetsProcessorTest : BaseTestConfig() {

    private val petService: PetService = Mockito.mock(PetService::class.java)
    private val solicitacaoService: SolicitacaoAdocaoService = Mockito.mock(SolicitacaoAdocaoService::class.java)

    lateinit var processor: ProcessorHandler<ObterPetsHandler>

    private val pageable = PageRequest.of(1,9)

    @BeforeEach
    fun setUp() {
        processor = ObterPetsProcessor(petService, solicitacaoService)
    }

    @Test
    @DisplayName("Criar handler com ID válido")
    fun `criar_handler_com_id_valido`() {
        val validToken = createToken(123L)
        val result = ObterPetsHandler.newOrProblem(validToken, pageable)

        assertTrue(result.isSuccess)
        val handler = result.getOrNull()
        assertEquals(handler?.id, 123L)
    }

    @Test
    @DisplayName("Criar handler com token nulo")
    fun `criar_handler_com_token_nulo`() {
        val result = ObterPetsHandler.newOrProblem(null, pageable)

        assertTrue(result.isSuccess)
        val handler = result.getOrNull()
        assertNull(handler?.id)
    }

    @Test
    @DisplayName("Obter todos os pets disponíveis quando o ID é nulo")
    fun `obter_todos_pets_disponiveis_quando_id_nulo`() {
        val validToken = createToken("Invalid")
        val handler: ObterPetsHandler = ObterPetsHandler.newOrProblem(validToken, pageable).getOrNull()!!

        val petList = listOf(mockPet(1L, "Pet 1"), mockPet(2L, "Pet 2"))
        val pages = PageImpl(petList)
        Mockito.`when`(petService.obterTodosDiponiveis(pageable)).thenReturn(pages)

        val response = processor.process(handler).getOrNull() as Page<Pet>
        assertEquals(petList.size, response.size)
        assertEquals(petList[0].nome, response.toList()[0].nome)
    }


    @Test
    @DisplayName("Obter pets de outros usuários com sucesso")
    fun `obter_pets_outros_usuarios_com_sucesso`() {
        val validToken = createToken(123L)
        val handler = ObterPetsHandler.newOrProblem(validToken, pageable).getOrElse { return }

        val petList = listOf(mockPet(1L, "Pet 1"), mockPet(2L, "Pet 2"))
        val pages = PageImpl(petList)
        Mockito.`when`(petService.obterPetsOutrosUsuarios(handler.id!!, pageable)).thenReturn(pages)

        val solicitacaoAdocao = mockSolicitacaoAdocao(1L, handler.id!!)
        Mockito.`when`(solicitacaoService.obterPorAdotanteIdPetId(any(), any())).thenReturn(solicitacaoAdocao)

        val response = processor.process(handler).getOrNull() as List<PetPost>
        assertEquals(petList.size, response.size)
        assertEquals(petList[0].nome, response[0].nome)
        assertEquals(response[0].status, petList[0].getStatus(solicitacaoAdocao))
    }

    @Test
    @DisplayName("Obter pets de outros usuários quando não há solicitações de adoção")
    fun `obter_pets_outros_usuarios_sem_solicitacoes_adocao`() {
        val validToken = createToken(123L)
        val handler = ObterPetsHandler.newOrProblem(validToken, pageable).getOrElse { return }
        val petList = listOf(mockPet(1L, "Pet 1"), mockPet(2L, "Pet 2"))
        val pages = PageImpl(petList)
        Mockito.`when`(petService.obterPetsOutrosUsuarios(handler.id!!, pageable)).thenReturn(pages)

        Mockito.`when`(solicitacaoService.obterPorAdotanteIdPetId(any(), any())).thenReturn(null)

        val response = processor.process(handler).getOrNull() as List<PetPost>
        assertEquals(petList.size, response.size)
        assertNull(response[0].status)
        assertEquals(petList[0].nome, response[0].nome)
    }

    // Funções auxiliares para mock de Pet e Solicitação de Adoção
    private fun mockPet(id: Long, nome: String): Pet {
        return Pet(
            id = id,
            nome = nome,
            descricao = "Descrição do $nome",
            sexo = Sexo.M,
            porte = Porte.M,
            castrado = Castrado.C,
            imageData = null,
            dataCadastro = LocalDateTime.now(),
            idUsuario = 12L,
            tipo = Tipo.C,
            idade = 1
        )
    }

    private fun mockSolicitacaoAdocao(petId: Long, usuarioId: Long): SolicitacaoAdocao {
        return SolicitacaoAdocao(
            id = UUID.randomUUID(), petId = petId, usuarioResponsavel = 2L, usuarioAdotante = usuarioId
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
