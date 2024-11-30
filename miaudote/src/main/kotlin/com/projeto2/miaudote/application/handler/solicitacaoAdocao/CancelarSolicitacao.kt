package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.*
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*
/**
 * Processador responsável por cancelar uma solicitação de adoção.
 *
 * @property service Serviço para gerenciar solicitações de adoção.
 * @property usuarioService Serviço para gerenciar operações relacionadas a usuários.
 * @property petService Serviço para gerenciar operações relacionadas a pets.
 * @property emailService Serviço para enviar e-mails.
 * @property adocaoService Serviço para gerenciar adoções.
 */
@Component
class CancelarSolicitacaoProcessor(
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val emailService: EmailService,
    val adocaoService: AdocaoService
) : ProcessorHandler<CancelarSolicitacaoHandler>() {
    /**
     * Processa a solicitação de cancelamento de adoção.
     *
     * @param handler Objeto contendo os dados necessários para o processamento.
     * @return Resultado do processo, indicando sucesso ou falha.
     */
    override fun process(handler: CancelarSolicitacaoHandler): Result<Any> {
        val solicitacaoAdocao = service.obterPorId(handler.solicitacaoId).toProblem().getOrElse {
            return Result.failure(it)
        }
        if (solicitacaoAdocao.dataConfirmacaoUserAdotante != null) return Result.failure(
            solicitacaoInvalida("Solicitação já confirmada pelo usuario adotante.", null)
        )
        if (adocaoService.obterPorSolicitacaoId(solicitacaoAdocao.id!!) != null) return Result.failure(
            solicitacaoInvalida("A solicitação já foi processada, e o animal já esta adotado.", null)
        )
        val pet = petService.obterPorId(solicitacaoAdocao.petId).toProblem().getOrElse {
            return Result.failure(it)
        }
        val responsavel = usuarioService.obterPorId(solicitacaoAdocao.usuarioResponsavel).toProblem().getOrElse {
            return Result.failure(it)
        }

        val adotante = usuarioService.obterPorId(solicitacaoAdocao.usuarioAdotante).toProblem().getOrElse {
            return Result.failure(it)
        }

        service.deletar(solicitacaoAdocao.id!!)

        emailService.enviarEmail(
            to = adotante.email,
            subject = "[MIAUDOTE] Solicitação Cancelada - ${pet.nome}",
            conteudo = geraConteudo(pet.nome)
        )
        emailService.enviarEmail(
            to = responsavel.email,
            subject = "[MIAUDOTE] Solicitação Cancelada com Sucesso - ${pet.nome}",
            conteudo = geraConteudo(pet.nome)
        )
        return Result.success("Sucesso!!")
    }
    /**
     * Gera o conteúdo do e-mail a ser enviado para o adotante e o responsável.
     *
     * @param nomePet Nome do pet.
     * @return Conteúdo do e-mail.
     */
    fun geraConteudo(nomePet: String): String {
        val conteudo = """
            A solicitação da adoção do animal $nomePet foi cancelada pelo responsável, 
            o animal continua disponível para adoção.
            
            Não responda este email.
        """.trimIndent()
        return conteudo
    }
}

/**
 * Handler para cancelar uma solicitação de adoção, contendo o ID da solicitação.
 *
 * @property solicitacaoId ID da solicitação de adoção.
 */
class CancelarSolicitacaoHandler private constructor(
    val solicitacaoId: UUID,
) : RequestHandler {
    companion object {
        /**
         * Cria uma nova instância do handler ou retorna um erro caso o ID seja inválido.
         *
         * @param solicitacaoIdIn ID da solicitação de adoção em formato String.
         * @return Resultado com a instância do handler ou um erro.
         */
        fun newOrProblem(solicitacaoIdIn: String): Result<CancelarSolicitacaoHandler> {
            val solicitacaoId = UUID.fromString(solicitacaoIdIn) ?: return Result.failure(
                solicitacaoInvalida("Id da solicitação da adoção inválida.", null)
            )
            return Result.success(
                CancelarSolicitacaoHandler(
                    solicitacaoId = solicitacaoId
                )
            )
        }
    }
}
/**
 * Cria um objeto Problem representando um erro ao tentar cancelar a solicitação de adoção.
 *
 * @param detail Detalhes sobre o erro.
 * @param extra Informações adicionais sobre o erro.
 * @return Um objeto Problem com o erro especificado.
 */
private fun solicitacaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel cancelar a solicitação de adoção.",
    detail = detail,
    type = URI("/cancelar-solicitacao-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)