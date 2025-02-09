package com.projeto2.miaudote.application.handler.adocao.acompanhamentoAdocao

import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.adocao.AcompanhamentoService
import com.projeto2.miaudote.application.services.adocao.AdocaoService
import com.projeto2.miaudote.application.services.adocao.PublicacaoService
import com.projeto2.miaudote.apresentation.request.adocao.acompanhamento.PublicacaoRequest
import com.projeto2.miaudote.domain.entities.Adocao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

class CriarAcompanhamentoProcessorTest {

    private val adocaoService: AdocaoService = mock()
    private val publicacaoService: PublicacaoService = mock()
    private val acompanhamentoService: AcompanhamentoService = mock()
    private lateinit var processor: CriarAcompanhamentoProcessor

    @BeforeEach
    fun setup() {
        processor = CriarAcompanhamentoProcessor(adocaoService, publicacaoService, acompanhamentoService)
    }

    @Test
    @DisplayName("Criar acompanhamento com sucesso")
    fun `criar_acompanhamento_com_sucesso`() {
        val petId = 123L
        val adocao = mockAdocao()
        val handler = CriarAcompanhamentoHandler.newOrProblem(
            PublicacaoRequest(mockMultipartFile(), "Descrição"),
            petId,
            mockJwtToken()
        ).getOrThrow()

        `when`(adocaoService.obterPorPetId(petId)).thenReturn(adocao)

        val result = processor.process(handler)

        assertTrue(result.isFailure)
        assertEquals("Não é possível processar essa ação", (result.exceptionOrNull() as Problem).detail)
        verify(publicacaoService).criar(anyOrNull())
        verify(acompanhamentoService).criar(anyOrNull())
    }

    @Test
    @DisplayName("Erro ao criar acompanhamento sem adoção registrada")
    fun `erro_ao_criar_acompanhamento_sem_adocao`() {
        val petId = 123L
        val handler = CriarAcompanhamentoHandler.newOrProblem(
            PublicacaoRequest(mockMultipartFile(), "Descrição"),
            petId,
            mockJwtToken()
        ).getOrThrow()

        `when`(adocaoService.obterPorPetId(petId)).thenReturn(null)

        val result = processor.process(handler)

        assertTrue(result.isFailure)
        assertEquals("Não existe adoção para os parametros informados.", (result.exceptionOrNull() as Problem).detail)
        verify(publicacaoService, never()).criar(anyOrNull())
        verify(acompanhamentoService, never()).criar(anyOrNull())
    }

    private fun mockAdocao(): Adocao {
        return mock()
    }

    private fun mockMultipartFile(): MockMultipartFile {
        return MockMultipartFile("image", "image.jpg", "image/jpeg", byteArrayOf(1, 2, 3))
    }

    private fun mockJwtToken(): JwtAuthenticationToken {
        val token: JwtAuthenticationToken = mock()
        `when`(token.name).thenReturn(UUID.randomUUID().toString())
        return token
    }
}

class CriarAcompanhamentoHandlerTest {

    @Test
    @DisplayName("Criar handler com sucesso")
    fun `criar_handler_com_sucesso`() {
        val request = PublicacaoRequest(mockMultipartFile(), "Descrição")
        val token = mockJwtToken()
        val petId = 123L

        val result = CriarAcompanhamentoHandler.newOrProblem(request, petId, token)

        assertTrue(result.isSuccess)
        assertEquals(petId, result.getOrNull()?.petId)
    }

    @Test
    @DisplayName("Erro ao criar handler sem imagem")
    fun `erro_ao_criar_handler_sem_imagem`() {
        val request = PublicacaoRequest(null, "Descrição")
        val token = mockJwtToken()
        val petId = 123L

        val result = CriarAcompanhamentoHandler.newOrProblem(request, petId, token)

        assertTrue(result.isFailure)
        assertEquals("Foto do Pet é obrigatória para o acompanhamento.", (result.exceptionOrNull() as Problem).detail)
    }

    private fun mockMultipartFile(): MockMultipartFile {
        return MockMultipartFile("image", "image.jpg", "image/jpeg", byteArrayOf(1, 2, 3))
    }

    private fun mockJwtToken(): JwtAuthenticationToken {
        val token: JwtAuthenticationToken = mock()
        `when`(token.name).thenReturn(UUID.randomUUID().toString())
        return token
    }
}
