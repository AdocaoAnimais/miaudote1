package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.*
import com.projeto2.miaudote.application.services.adocao.AdocaoService
import com.projeto2.miaudote.application.services.adocao.SolicitacaoAdocaoService
import com.projeto2.miaudote.domain.entities.Adocao
import com.projeto2.miaudote.domain.entities.Pet
import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI
import java.time.LocalDateTime
import java.util.*
/**
 * Processador responsável por confirmar a adoção de um pet.
 *
 * @property service Serviço para gerenciar solicitações de adoção.
 * @property usuarioService Serviço para gerenciar operações relacionadas a usuários.
 * @property petService Serviço para gerenciar operações relacionadas a pets.
 * @property adocaoService Serviço para gerenciar adoções.
 * @property emailService Serviço para enviar e-mails.
 */
@Component
class ConfirmarAdocaoProcessor(
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val adocaoService: AdocaoService,
    val emailService: EmailService,
) : ProcessorHandler<ConfirmarAdocaoHandler>() {
    /**
     * Processa a solicitação de confirmação da adoção de um pet.
     *
     * @param handler Objeto contendo os dados necessários para o processamento.
     * @return Resultado do processo, indicando sucesso ou falha.
     */
    override fun process(handler: ConfirmarAdocaoHandler): Result<Any> {
        val solicitacao = service.obterPorId(handler.solicitacaoId).toProblem().getOrElse {
            return Result.failure(it)
        }
        val pet = petService.obterPorId(solicitacao.petId).toProblem().getOrElse {
            return Result.failure(it)
        }
        if (adocaoService.obterPorPetId(pet.id!!) != null) return Result.failure(
            adocaoInvalida("O pet ${pet.nome} já foi adotado.", null)
        )

        val adotante = usuarioService.obterPorId(solicitacao.usuarioAdotante).toProblem().getOrElse {
            return Result.failure(it)
        }
        val responsavel = usuarioService.obterPorId(solicitacao.usuarioResponsavel).toProblem().getOrElse {
            return Result.failure(it)
        }
        val solicitacaoAtualizada = solicitacao.copy(
            dataConfirmacaoUserAdotante = LocalDateTime.now()
        )
        service.atualizar(solicitacaoAtualizada)

        val adocao = Adocao(
            id = null,
            petId = pet.id,
            dataAdocao = LocalDateTime.now(),
            solicitacaoId = solicitacao.id!!
        )

        adocaoService.criar(adocao)

        notificarResponsavel(responsavel = responsavel, pet = pet)

        notificarAdotante(adotante = adotante, pet = pet)

        return Result.success("Sucesso!!")
    }
    /**
     * Notifica o responsável pela adoção do pet que a adoção foi concluída com sucesso.
     *
     * @param responsavel Usuário responsável pela adoção.
     * @param pet Pet que foi adotado.
     */
    private fun notificarResponsavel(responsavel: Usuario, pet: Pet) {
        val conteudo = """
            Recebemos a confirmação do novo tutor de ${pet.nome} que a adoção foi concluída com sucesso.
            Então ${pet.nome} não esta mais dísponivel para adoção!!
        """.trimIndent()
        emailService.enviarEmail(
            to = responsavel.email,
            subject = "[MIAUDOTE] Adoção Concluída com SUCESSO! - ${pet.nome}",
            conteudo = conteudo
        )
    }
    /**
     * Notifica o adotante do pet que a adoção foi concluída com sucesso.
     *
     * @param adotante Usuário adotante do pet.
     * @param pet Pet que foi adotado.
     */
    private fun notificarAdotante(adotante: Usuario, pet: Pet) {
        val conteudo = """
            Recebemos a confirmação da adoção de ${pet.nome} foi concluída com sucesso.
            Então ${pet.nome} não esta mais dísponivel para adoção!!
        """.trimIndent()
        emailService.enviarEmail(
            to = adotante.email,
            subject = "[MIAUDOTE] Adoção Concluída com SUCESSO! - ${pet.nome}",
            conteudo = conteudo
        )
    }
}

/**
 * Handler para confirmar a adoção de um pet, contendo o ID da solicitação.
 *
 * @property solicitacaoId ID da solicitação de adoção.
 */
class ConfirmarAdocaoHandler private constructor(
    val solicitacaoId: UUID,
) : RequestHandler {
    companion object {
        /**
         * Cria uma nova instância do handler ou retorna um erro caso o ID seja inválido.
         *
         * @param solicitacaoIdIn ID da solicitação de adoção em formato String.
         * @return Resultado com a instância do handler ou um erro.
         */
        fun newOrProblem(solicitacaoIdIn: String): Result<ConfirmarAdocaoHandler> {
            val solicitacaoId = UUID.fromString(solicitacaoIdIn) ?: return Result.failure(
                adocaoInvalida("Id da solicitação de adoção inválida.", null)
            )
            return Result.success(
                ConfirmarAdocaoHandler(
                    solicitacaoId = solicitacaoId,
                )
            )
        }
    }
}
/**
 * Cria um objeto Problem representando um erro ao tentar confirmar a adoção.
 *
 * @param detail Detalhes sobre o erro.
 * @param extra Informações adicionais sobre o erro.
 * @return Um objeto Problem com o erro especificado.
 */
private fun adocaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel confirmar a adoção.",
    detail = detail,
    type = URI("/confirmar-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)