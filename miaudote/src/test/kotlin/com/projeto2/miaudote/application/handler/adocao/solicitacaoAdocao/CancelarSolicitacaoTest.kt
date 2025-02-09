package com.projeto2.miaudote.application.handler.adocao.solicitacaoAdocao

import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.adocao.AdocaoService
import com.projeto2.miaudote.application.services.adocao.SolicitacaoAdocaoService
import com.projeto2.miaudote.application.services.external.mail.EmailService
import com.projeto2.miaudote.application.services.pet.PetService
import com.projeto2.miaudote.application.services.usuario.UsuarioService
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.domain.entities.pet.Pet
import com.projeto2.miaudote.domain.entities.usuario.Usuario
import com.projeto2.miaudote.domain.enums.Castrado
import com.projeto2.miaudote.domain.enums.Porte
import com.projeto2.miaudote.domain.enums.Sexo
import com.projeto2.miaudote.domain.enums.Tipo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import java.time.LocalDateTime
import java.util.*

class CancelarSolicitacaoProcessorTest {

    private val solicitacaoService: SolicitacaoAdocaoService = mock()
    private val usuarioService: UsuarioService = mock()
    private val petService: PetService = mock()
    private val emailService: EmailService = mock()
    private val adocaoService: AdocaoService = mock()
    private lateinit var processor: CancelarSolicitacaoProcessor

    @BeforeEach
    fun setup() {
        processor = CancelarSolicitacaoProcessor(
            solicitacaoService,
            usuarioService,
            petService,
            emailService,
            adocaoService
        )
    }

    @Test
    @DisplayName("Cancelar solicitação com sucesso")
    fun `cancelar_solicitacao_com_sucesso`() {
        val solicitacaoId = UUID.randomUUID()
        val solicitacao = mockSolicitacao(solicitacaoId)
        val pet = mockPet(4L)
        val adotante = mockUsuario(5L)
        val responsavel = mockUsuario(6L)

        `when`(solicitacaoService.obterPorId(solicitacaoId)).thenReturn(solicitacao)
        `when`(petService.obterPorId(solicitacao.petId)).thenReturn(pet)
        `when`(usuarioService.obterPorId(solicitacao.usuarioAdotante)).thenReturn(adotante)
        `when`(usuarioService.obterPorId(solicitacao.usuarioResponsavel)).thenReturn(responsavel)
        `when`(adocaoService.obterPorSolicitacaoId(solicitacaoId)).thenReturn(null)

        val handler = CancelarSolicitacaoHandler.newOrProblem(solicitacaoId.toString()).getOrThrow()
        val result = processor.process(handler)

        assertTrue(result.isSuccess)
        verify(solicitacaoService).deletar(solicitacaoId)
        verify(emailService, times(2)).enviarEmail(anyString(), anyString(), anyString())
    }

    @Test
    @DisplayName("Erro ao cancelar solicitação já confirmada")
    fun `erro_ao_cancelar_solicitacao_ja_confirmada`() {
        val solicitacaoId = UUID.randomUUID()
        val solicitacao = mockSolicitacao(solicitacaoId, confirmado = true)

        `when`(solicitacaoService.obterPorId(solicitacaoId)).thenReturn(solicitacao)

        val handler = CancelarSolicitacaoHandler.newOrProblem(solicitacaoId.toString()).getOrThrow()
        val result = processor.process(handler)

        assertTrue(result.isFailure)
        assertEquals("Solicitação já confirmada pelo usuario adotante.", (result.exceptionOrNull() as Problem).detail)
        verify(solicitacaoService, never()).deletar(solicitacaoId)
    }

    @Test
    @DisplayName("Erro ao cancelar solicitação já processada com adoção")
    fun `erro_ao_cancelar_solicitacao_ja_processada_com_adocao`() {
        val solicitacaoId = UUID.randomUUID()
        val solicitacao = mockSolicitacao(solicitacaoId)

        `when`(solicitacaoService.obterPorId(solicitacaoId)).thenReturn(solicitacao)
        `when`(adocaoService.obterPorSolicitacaoId(solicitacaoId)).thenReturn(mock())

        val handler = CancelarSolicitacaoHandler.newOrProblem(solicitacaoId.toString()).getOrThrow()
        val result = processor.process(handler)

        assertTrue(result.isFailure)
        assertEquals(
            "A solicitação já foi processada, e o animal já esta adotado.",
            (result.exceptionOrNull() as Problem).detail
        )
        verify(solicitacaoService, never()).deletar(solicitacaoId)
    }

    @Test
    @DisplayName("Erro ao cancelar solicitação com pet inexistente")
    fun `erro_ao_cancelar_solicitacao_com_pet_inexistente`() {
        val solicitacaoId = UUID.randomUUID()
        val solicitacao = mockSolicitacao(solicitacaoId)

        `when`(solicitacaoService.obterPorId(solicitacaoId)).thenReturn(solicitacao)
        `when`(petService.obterPorId(solicitacao.petId)).thenReturn(null)

        val handler = CancelarSolicitacaoHandler.newOrProblem(solicitacaoId.toString()).getOrThrow()
        val result = processor.process(handler)

        assertTrue(result.isFailure)
        assertEquals("O pet com id informado não esta cadastrado", (result.exceptionOrNull() as Problem).detail)
        verify(solicitacaoService, never()).deletar(solicitacaoId)
    }

    // Funções auxiliares para mocks
    private fun mockSolicitacao(id: UUID, confirmado: Boolean = false): SolicitacaoAdocao {
        return SolicitacaoAdocao(
            id = id,
            petId = 2L,
            usuarioAdotante = 1L,
            usuarioResponsavel = 2L
        ).copy(
            id = id,
            petId = 2L,
            usuarioAdotante = 3L,
            usuarioResponsavel = 4L,
            dataConfirmacaoUserAdotante = if (confirmado) LocalDateTime.now() else null
        )
    }

    private fun mockPet(id: Long): Pet {
        return Pet(
            id = id,
            nome = "Rex",
            idade = 2,
            porte = Porte.M,
            sexo = Sexo.M,
            tipo = Tipo.C,
            castrado = Castrado.C,
            descricao = "Descrição do pet",
            idUsuario = 123L,
            dataCadastro = LocalDateTime.now(),
            imageData = null,
        ).copy(
            nome = "testeom"
        )
    }

    private fun mockUsuario(id: Long): Usuario {
        return Usuario(
            id = id,
            nome = "Nome",
            sobrenome = "Sobrenome",
            username = "user",
            email = "user@teste.com",
            cpf = "12345678900",
            descricao = "Descrição",
            contato = "999999999",
            endereco = null,
            emailVerificado = true,
            senha = "senhaNova"
        ).copy(
            email = "teste@miaudote.com"
        )
    }
}
