package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.*
import com.projeto2.miaudote.application.services.adocao.AdocaoService
import com.projeto2.miaudote.application.services.adocao.SolicitacaoAdocaoService
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI
import java.time.LocalDateTime
import java.util.*
/**
 * Processador responsável por confirmar a solicitação de adoção de um pet.
 *
 * @property service Serviço para gerenciar solicitações de adoção.
 * @property usuarioService Serviço para gerenciar operações relacionadas a usuários.
 * @property petService Serviço para gerenciar operações relacionadas a pets.
 * @property emailService Serviço para enviar e-mails.
 * @property adocaoService Serviço para gerenciar adoções.
 * @property baseUrl URL base da aplicação, utilizada para gerar links de confirmação e cancelamento.
 */
@Component
class ConfirmarSolicitacaoProcessor(
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val emailService: EmailService,
    val adocaoService: AdocaoService,
    @Value("\${base.url}")
    private val baseUrl: String
) : ProcessorHandler<ConfirmarSolicitacaoHandler>() {
    /**
     * Processa a solicitação de confirmação da adoção de um pet.
     *
     * @param handler Objeto contendo os dados necessários para o processamento.
     * @return Resultado do processo, indicando sucesso ou falha.
     */
    override fun process(handler: ConfirmarSolicitacaoHandler): Result<Any> {
        val solicitacaoAdocao = service.obterPorId(handler.solicitacaoId).toProblem().getOrElse {
            return Result.failure(it)
        }
        val pet = petService.obterPorId(solicitacaoAdocao.petId).toProblem().getOrElse {
            return Result.failure(it)
        }
        if (adocaoService.obterPorPetId(pet.id!!) != null) return Result.failure(
            solicitacaoInvalida("O pet ${pet.nome} já foi adotado.", null)
        )
        val responsavel = usuarioService.obterPorId(solicitacaoAdocao.usuarioResponsavel).toProblem().getOrElse {
            return Result.failure(it)
        }
        val adotante = usuarioService.obterPorId(solicitacaoAdocao.usuarioAdotante).toProblem().getOrElse {
            return Result.failure(it)
        }
        val solicitacaoAtualizada = solicitacaoAdocao.update(
            dataConfirmacaoUserResponsavel = LocalDateTime.now(),
            dataConfirmacaoUserAdotante = null
        )

        emailService.enviarEmailUsuarioAdotante(
            adotante = adotante,
            pet = pet,
            linkConfirmaAdocao = "$baseUrl/solicitacao-adocao/confirmar-adocao/${solicitacaoAdocao.id}",
            linkCancelaAdocao = "$baseUrl/solicitacao-adocao/cancelar-adocao/${solicitacaoAdocao.id}",
            responsavel = responsavel
        )
        service.atualizar(solicitacaoAtualizada)
        return Result.success("Sucesso!!")
    }
}

/**
 * Handler para confirmar a solicitação de adoção de um pet, contendo o ID da solicitação.
 *
 * @property solicitacaoId ID da solicitação de adoção.
 */
class ConfirmarSolicitacaoHandler private constructor(
    val solicitacaoId: UUID,
) : RequestHandler {
    companion object {
        /**
         * Cria uma nova instância do handler ou retorna um erro caso o ID seja inválido.
         *
         * @param solicitacaoIdIn ID da solicitação de adoção em formato String.
         * @return Resultado com a instância do handler ou um erro.
         */
        fun newOrProblem(solicitacaoIdIn: String): Result<ConfirmarSolicitacaoHandler> {
            val solicitacaoId = UUID.fromString(solicitacaoIdIn) ?: return Result.failure(
                solicitacaoInvalida("Id da solicitação da adoção inválido.", null)
            )
            return Result.success(
                ConfirmarSolicitacaoHandler(
                    solicitacaoId = solicitacaoId,
                )
            )
        }
    }
}

/**
 * Cria um objeto Problem representando um erro ao tentar confirmar a solicitação de adoção.
 *
 * @param detail Detalhes sobre o erro.
 * @param extra Informações adicionais sobre o erro.
 * @return Um objeto Problem com o erro especificado.
 */
private fun solicitacaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel confirmar a solicitação de adoção.",
    detail = detail,
    type = URI("/confirmar-solicitacao-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)