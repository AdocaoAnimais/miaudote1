package com.projeto2.miaudote.application.handler.adocao.solicitacaoAdocao

import org.junit.jupiter.api.Assertions.*
import com.projeto2.miaudote.BaseTestConfig
import com.projeto2.miaudote.application.handler.adocao.solicitacaoAdocao.CancelarAdocaoHandler
import com.projeto2.miaudote.application.handler.adocao.solicitacaoAdocao.CancelarAdocaoProcessor
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.domain.entities.pet.Pet
import com.projeto2.miaudote.domain.entities.usuario.Usuario
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.adocao.AdocaoService
import com.projeto2.miaudote.application.services.adocao.SolicitacaoAdocaoService
import com.projeto2.miaudote.application.services.external.mail.EmailService
import com.projeto2.miaudote.application.services.pet.PetService
import com.projeto2.miaudote.application.services.usuario.UsuarioService
import com.projeto2.miaudote.domain.enums.Castrado
import com.projeto2.miaudote.domain.enums.Porte
import com.projeto2.miaudote.domain.enums.Sexo
import com.projeto2.miaudote.domain.enums.Tipo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import java.time.LocalDateTime
import java.util.*

class CancelarAdocaoProcessorTest : BaseTestConfig() {

    private val solicitacaoAdocaoService: SolicitacaoAdocaoService = Mockito.mock(SolicitacaoAdocaoService::class.java)
    private val usuarioService: UsuarioService = Mockito.mock(UsuarioService::class.java)
    private val petService: PetService = Mockito.mock(PetService::class.java)
    private val emailService: EmailService = Mockito.mock(EmailService::class.java)
    private val adocaoService: AdocaoService = Mockito.mock(AdocaoService::class.java)

    private lateinit var processor: CancelarAdocaoProcessor

    @BeforeEach
    fun setUp() {
        processor =
            CancelarAdocaoProcessor(solicitacaoAdocaoService, usuarioService, petService, emailService, adocaoService)
    }

    @Test
    @DisplayName("Cancelar adoção com sucesso")
    fun `cancelar_adocao_com_sucesso`() {
        val solicitacaoId = UUID.randomUUID()
        val solicitacao = mockSolicitacao(solicitacaoId)
        val pet = mockPet(solicitacao.petId)
        val adotante = mockUsuario(solicitacao.usuarioAdotante)
        val responsavel = mockUsuario(solicitacao.usuarioResponsavel)

        Mockito.`when`(solicitacaoAdocaoService.obterPorId(solicitacaoId)).thenReturn(solicitacao)
        Mockito.`when`(petService.obterPorId(solicitacao.petId)).thenReturn(pet)
        Mockito.`when`(usuarioService.obterPorId(solicitacao.usuarioAdotante)).thenReturn(adotante)
        Mockito.`when`(usuarioService.obterPorId(solicitacao.usuarioResponsavel)).thenReturn(responsavel)

        val handler = CancelarAdocaoHandler.newOrProblem(solicitacao.id.toString()).getOrNull()!!
        val result = processor.process(handler)

        assertTrue(result.isSuccess)
        Mockito.verify(solicitacaoAdocaoService).deletar(solicitacaoId)
        Mockito.verify(emailService, Mockito.times(2)).enviarEmail(any(), any(), any())
    }

    @Test
    @DisplayName("Erro ao obter solicitação de adoção inválida")
    fun `erro_ao_obter_solicitacao_adocao_invalida`() {
        val solicitacaoId = UUID.randomUUID()
        val solicitacao = mockSolicitacao(solicitacaoId)
        Mockito.`when`(solicitacaoAdocaoService.obterPorId(solicitacaoId)).thenReturn(null)

        val handler = CancelarAdocaoHandler.newOrProblem(solicitacao.id.toString()).getOrNull()!!
        val result = processor.process(handler)

        assertTrue(result.isFailure)
        assertEquals(
            "Solicitação de adoção não encontrada",
            (result.exceptionOrNull() as Problem).title
        )
    }

    @Test
    @DisplayName("Erro ao obter pet inexistente")
    fun `erro_ao_obter_pet_inexistente`() {
        val solicitacaoId = UUID.randomUUID()
        val solicitacao = mockSolicitacao(solicitacaoId)

        Mockito.`when`(solicitacaoAdocaoService.obterPorId(solicitacaoId)).thenReturn(solicitacao)
        Mockito.`when`(petService.obterPorId(solicitacao.petId)).thenReturn(null)

        val handler = CancelarAdocaoHandler.newOrProblem(solicitacao.id.toString()).getOrNull()!!
        val result = processor.process(handler)

        assertTrue(result.isFailure)
        assertEquals(
            "Pet não encontrado",
            (result.exceptionOrNull() as Problem).title
        )
    }

    @Test
    @DisplayName("Erro ao obter usuário adotante inexistente")
    fun `erro_ao_obter_usuario_adotante_inexistente`() {
        val solicitacaoId = UUID.randomUUID()
        val solicitacao = mockSolicitacao(solicitacaoId)
        val pet = mockPet(solicitacao.petId)

        Mockito.`when`(solicitacaoAdocaoService.obterPorId(solicitacaoId)).thenReturn(solicitacao)
        Mockito.`when`(petService.obterPorId(solicitacao.petId)).thenReturn(pet)
        Mockito.`when`(usuarioService.obterPorId(solicitacao.usuarioAdotante)).thenReturn(null)

        val handler = CancelarAdocaoHandler.newOrProblem(solicitacao.id.toString()).getOrNull()!!
        val result = processor.process(handler)

        assertTrue(result.isFailure)
        assertEquals(
            "Usuário não encontrado",
            (result.exceptionOrNull() as Problem).title
        )
    }

    @Test
    @DisplayName("Erro ao obter usuário responsável inexistente")
    fun `erro_ao_obter_usuario_responsavel_inexistente`() {
        val solicitacaoId = UUID.randomUUID()
        val solicitacao = mockSolicitacao(solicitacaoId)
        val pet = mockPet(solicitacao.petId)
        val adotante = mockUsuario(solicitacao.usuarioAdotante)

        Mockito.`when`(solicitacaoAdocaoService.obterPorId(solicitacaoId)).thenReturn(solicitacao)
        Mockito.`when`(petService.obterPorId(solicitacao.petId)).thenReturn(pet)
        Mockito.`when`(usuarioService.obterPorId(solicitacao.usuarioAdotante)).thenReturn(adotante)
        Mockito.`when`(usuarioService.obterPorId(solicitacao.usuarioResponsavel)).thenReturn(null)

        val handler = CancelarAdocaoHandler.newOrProblem(solicitacao.id.toString()).getOrNull()!!
        val result = processor.process(handler)

        assertTrue(result.isFailure)
        assertEquals(
            "Usuário não encontrado",
            (result.exceptionOrNull() as Problem).title
        )
    }

    @Test
    @DisplayName("Cancelar adoção com ID válido")
    fun `cancelar_adocao_com_id_valido`() {
        val solicitacao = mockSolicitacao(UUID.randomUUID())
        val pet = mockPet(solicitacao.petId)
        val adotante = mockUsuario(solicitacao.usuarioAdotante)
        val responsavel = mockUsuario(solicitacao.usuarioResponsavel)

        Mockito.`when`(solicitacaoAdocaoService.obterPorId(solicitacao.id!!)).thenReturn(solicitacao)
        Mockito.`when`(petService.obterPorId(solicitacao.petId)).thenReturn(pet)
        Mockito.`when`(usuarioService.obterPorId(solicitacao.usuarioAdotante)).thenReturn(adotante)
        Mockito.`when`(usuarioService.obterPorId(solicitacao.usuarioResponsavel)).thenReturn(responsavel)

        val handler = CancelarAdocaoHandler.newOrProblem(solicitacao.id.toString()).getOrNull()!!
        val result = processor.process(handler)

        assertTrue(result.isSuccess)
    }

    @Test
    @DisplayName("Cancelar adoção com ID de solicitação inválido")
    fun `cancelar_adocao_com_id_solicitacao_invalido`() {
        val invalidId = "invalid-uuid"
        val handlerResult = CancelarAdocaoHandler.newOrProblem(invalidId)

        assertTrue(handlerResult.isFailure)
        assertEquals("Id da solicitação da adoção inválida.", (handlerResult.exceptionOrNull() as Problem).detail)
    }

    @Test
    @DisplayName("Cancelar adoção sem registro de adoção")
    fun `cancelar_adocao_sem_registro_adocao`() {
        val solicitacao = mockSolicitacao(UUID.randomUUID())
        val pet = mockPet(solicitacao.petId)
        val adotante = mockUsuario(solicitacao.usuarioAdotante)
        val responsavel = mockUsuario(solicitacao.usuarioResponsavel)

        Mockito.`when`(solicitacaoAdocaoService.obterPorId(solicitacao.id!!)).thenReturn(solicitacao)
        Mockito.`when`(petService.obterPorId(solicitacao.petId)).thenReturn(pet)
        Mockito.`when`(adocaoService.obterPorSolicitacaoId(solicitacao.id!!)).thenReturn(null)
        Mockito.`when`(usuarioService.obterPorId(solicitacao.usuarioAdotante)).thenReturn(adotante)
        Mockito.`when`(usuarioService.obterPorId(solicitacao.usuarioResponsavel)).thenReturn(responsavel)
        Mockito.`when`(emailService.enviarEmail(any(), any(), any()))
            .thenReturn(Result.success("Enviado com sucesso"))

        val handler = CancelarAdocaoHandler.newOrProblem(solicitacao.id.toString()).getOrNull()!!
        val result = processor.process(handler)

        assertTrue(result.isSuccess)
        Mockito.verify(adocaoService, Mockito.never()).deletar(any())
    }

    @Test
    @DisplayName("Gerar conteúdo de email corretamente")
    fun `gerar_conteudo_email_corretamente`() {
        val nomePet = "Rex"
        val nomeAdotante = "João"
        val sobrenome = "Silva"

        val conteudo = processor.geraConteudo(nomePet, nomeAdotante, sobrenome)

        assertTrue(conteudo.contains(nomePet))
        assertTrue(conteudo.contains(nomeAdotante))
        assertTrue(conteudo.contains(sobrenome))
    }

    // Funções auxiliares para mocks
    private fun mockSolicitacao(id: UUID): SolicitacaoAdocao {
        return SolicitacaoAdocao(
            id = id,
            petId = 2L,
            usuarioAdotante = 1L,
            usuarioResponsavel = 2L
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
        )
    }
}
