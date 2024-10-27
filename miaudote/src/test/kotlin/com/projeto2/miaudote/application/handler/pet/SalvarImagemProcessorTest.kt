package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.BaseTestConfig
import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.PetService
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
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.time.Instant
import java.time.LocalDateTime

class SalvarImagemProcessorTest : BaseTestConfig() {

    private val petService: PetService = Mockito.mock(PetService::class.java)

    lateinit var processorWithMock: ProcessorHandler<SalvarImagemHandler>

    @Autowired
    lateinit var processor: ProcessorHandler<SalvarImagemHandler>

    @BeforeEach
    fun setUp() {
        this.processorWithMock = SalvarImagemProcessor(petService)
    }

    @Test
    @DisplayName("Falha ao converter ID de usuário inválido")
    fun `id_usuario_invalido`() {
        val invalidToken = createToken("invalid-id")

        val result = SalvarImagemHandler.newOrProblem(1L, mockValidImageFile(), invalidToken)

        assertTrue(result.isFailure)
        val problem = result.exceptionOrNull() as Problem
        assertEquals(problem.detail, "Id do usuário não encontrado.")
        assertEquals(problem.title, "Não foi possivel salvar a imagem")
        assertEquals(problem.type, URI("/salvar-imagem"))
    }

    @Test
    @DisplayName("Pet não encontrado para salvar imagem")
    fun `pet_nao_encontrado`() {
        val validToken = createToken(123L)

        Mockito.`when`(petService.obterPorId(any())).thenReturn(null)

        val result = SalvarImagemHandler.newOrProblem(1L, mockValidImageFile(), validToken)
        assertTrue(result.isSuccess)

        val handler = result.getOrElse { return }
        val response = processorWithMock.process(handler).exceptionOrNull()

        val problem = response as Problem
        assertEquals(problem.detail, "Pet não encontrado")
        assertEquals(problem.title, "Não foi possivel salvar a imagem")
        assertEquals(problem.type, URI("/salvar-imagem"))
    }

    @Test
    @DisplayName("Usuário sem permissão para atualizar a imagem do pet")
    fun `usuario_sem_permissao_para_atualizar_imagem`() {
        val validToken = createToken(123L)

        val pet = mockPet(usuarioId = 999L)  // Pet pertencente a outro usuário
        Mockito.`when`(petService.obterPorId(any())).thenReturn(pet)

        val result = SalvarImagemHandler.newOrProblem(1L, mockValidImageFile(), validToken)
        assertTrue(result.isSuccess)

        val handler = result.getOrElse { return }
        val response = processorWithMock.process(handler).exceptionOrNull()

        val problem = response as Problem
        assertEquals(problem.detail, "Usuário não tem permissão para atualizar este pet")
        assertEquals(problem.title, "Não foi possivel salvar a imagem")
        assertEquals(problem.type, URI("/salvar-imagem"))
    }

    @Test
    @DisplayName("Falha ao salvar imagem - Formato incorreto")
    fun `imagem_formato_incorreto`() {
        val validToken = createToken(123L)

        val pet = mockPet()
        Mockito.`when`(petService.obterPorId(any())).thenReturn(pet)

        val invalidImageFile = mockImageFile(contentType = "image/gif")
        val result = SalvarImagemHandler.newOrProblem(1L, invalidImageFile, validToken)
        assertTrue(result.isSuccess)

        val handler = result.getOrElse { return }
        val response = processorWithMock.process(handler).exceptionOrNull()

        val problem = response as Problem
        assertEquals(problem.detail, "Imagem em formato incorreto")
        assertEquals(problem.title, "Não foi possivel salvar a imagem")
        assertEquals(problem.type, URI("/salvar-imagem"))
    }

    @Test
    @DisplayName("Falha ao salvar imagem - Tamanho excedido")
    fun `imagem_tamanho_excedido`() {
        val validToken = createToken(123L)

        val pet = mockPet()
        Mockito.`when`(petService.obterPorId(any())).thenReturn(pet)

        val oversizedImageFile = mockImageFile(size = 17000000)  // Imagem maior que 16MB
        val result = SalvarImagemHandler.newOrProblem(1L, oversizedImageFile, validToken)
        assertTrue(result.isSuccess)

        val handler = result.getOrElse { return }
        val response = processorWithMock.process(handler).exceptionOrNull()

        val problem = response as Problem
        assertEquals(problem.detail, "Imagem excede o tamanho de 16MB")
        assertEquals(problem.title, "Não foi possivel salvar a imagem")
        assertEquals(problem.type, URI("/salvar-imagem"))
    }

    @Test
    @DisplayName("Salvar imagem com sucesso")
    fun `salvar_imagem_com_sucesso`() {
        val validToken = createToken(123L)

        val pet = mockPet()
        Mockito.`when`(petService.obterPorId(any())).thenReturn(pet)
        Mockito.`when`(petService.atualizar(any())).thenReturn(pet)

        val validImageFile = mockValidImageFile()
        val result = SalvarImagemHandler.newOrProblem(1L, validImageFile, validToken)
        assertTrue(result.isSuccess)

        val handler = result.getOrElse { return }
        val response = processorWithMock.process(handler).getOrNull()

        assertEquals(response, pet)
    }

    @Test
    @DisplayName("Criação de handler com sucesso")
    fun `criacao_handler_com_sucesso`() {
        val validToken = createToken(123L)

        val result = SalvarImagemHandler.newOrProblem(1L, mockValidImageFile(), validToken)

        assertTrue(result.isSuccess)

        val handler = result.getOrNull()!!
        assertEquals(handler.id, 1L)
        assertEquals(handler.token.name.toLong(), 123L)
    }

    // Funções auxiliares para mock de Pet, ImageFile e Token
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

    private fun mockValidImageFile(): MultipartFile {
        val imageBytes = ByteArray(1000000)  // Tamanho de 1MB
        return MockMultipartFile(
            "imagem",
            "imagem.jpg",
            "image/jpeg",
            imageBytes
        )
    }

    private fun mockImageFile(contentType: String? = "image/jpeg", size: Int = 1000000): MultipartFile {
        val imageBytes = ByteArray(size)
        return MockMultipartFile(
            "imagem",
            "imagem.jpg",
            contentType,
            imageBytes
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
