package com.projeto2.miaudote.application.handler.adocao.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.adocao.AdocaoService
import com.projeto2.miaudote.application.services.adocao.SolicitacaoAdocaoService
import com.projeto2.miaudote.application.services.external.mail.EmailService
import com.projeto2.miaudote.application.services.pet.PetService
import com.projeto2.miaudote.application.services.usuario.UsuarioService
import com.projeto2.miaudote.domain.entities.pet.toProblem
import com.projeto2.miaudote.domain.entities.toProblem
import com.projeto2.miaudote.shared.toUUID
import com.projeto2.miaudote.domain.entities.usuario.toProblem
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*
/**
 * Processador responsável por cancelar uma adoção.
 *
 * @property service Serviço para gerenciar solicitações de adoção.
 * @property usuarioService Serviço para gerenciar operações relacionadas a usuários.
 * @property petService Serviço para gerenciar operações relacionadas a pets.
 * @property emailService Serviço para enviar e-mails.
 * @property adocaoService Serviço para gerenciar adoções.
 */
@Component
class CancelarAdocaoProcessor(
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val emailService: EmailService,
    val adocaoService: AdocaoService
) : ProcessorHandler<CancelarAdocaoHandler>() {
    /**
     * Processa a solicitação de cancelamento de adoção.
     *
     * @param handler Objeto contendo os dados necessários para o processamento.
     * @return Resultado do processo, indicando sucesso ou falha.
     */
    override fun process(handler: CancelarAdocaoHandler): Result<Any> {
        val solicitacao = service.obterPorId(handler.solicitacaoAdocaoId).toProblem().getOrElse {
            return Result.failure(it)
        }
        val pet = petService.obterPorId(solicitacao.petId).toProblem().getOrElse {
            return Result.failure(it)
        }
        val adocao = adocaoService.obterPorSolicitacaoId(solicitacaoId = solicitacao.id!!)
        if (adocao != null) {
            adocaoService.deletar(adocao)
        }
        val adotante = usuarioService.obterPorId(solicitacao.usuarioAdotante).toProblem().getOrElse {
            return Result.failure(it)
        }
        val responsavel = usuarioService.obterPorId(solicitacao.usuarioResponsavel).toProblem().getOrElse {
            return Result.failure(it)
        }

        service.deletar(solicitacao.id)

        emailService.enviarEmail(
            to = responsavel.email,
            subject = "[MIAUDOTE] Adoção Cancelada - ${pet.nome}",
            conteudo = geraConteudo(pet.nome, adotante.nome, adotante.sobrenome)
        )
        emailService.enviarEmail(
            to = adotante.email,
            subject = "[MIAUDOTE] Adoção Cancelada com Sucesso- ${pet.nome}",
            conteudo = geraConteudo(pet.nome, adotante.nome, adotante.sobrenome)
        )

        return Result.success("Adoção cancelada com sucesso!!")
    }
    /**
     * Gera o conteúdo do e-mail a ser enviado para o adotante e o responsável.
     *
     * @param nomePet Nome do pet.
     * @param nomeAdotante Nome do adotante.
     * @param sobrenome Sobrenome do adotante.
     * @return Conteúdo do e-mail.
     */
    fun geraConteudo(nomePet: String, nomeAdotante: String, sobrenome: String): String {
        val conteudo = """
            A adoção do animal $nomePet foi cancelada pelo adotante $nomeAdotante $sobrenome, 
            o animal continua disponível para adoção.
            
            Não responda este email.
        """.trimIndent()
        return conteudo
    }
}
/**
 * Handler para cancelar uma adoção, contendo o ID da solicitação de adoção.
 *
 * @property solicitacaoAdocaoId ID da solicitação de adoção.
 */
class CancelarAdocaoHandler private constructor(
    val solicitacaoAdocaoId: UUID,
) : RequestHandler {
    companion object {
        /**
         * Cria uma nova instância do handler ou retorna um erro caso o ID seja inválido.
         *
         * @param solicitacaoAdocaoIdIn ID da solicitação de adoção em formato String.
         * @return Resultado com a instância do handler ou um erro.
         */
        fun newOrProblem(solicitacaoAdocaoIdIn: String): Result<CancelarAdocaoHandler> {
            val solicitacaoAdocaoId = solicitacaoAdocaoIdIn.toUUID().getOrElse {
                it as Problem
                return Result.failure(
                    adocaoInvalida(
                        "Id da solicitação da adoção inválida.",
                        mapOf(Pair("solicitacaoAdocaoId", solicitacaoAdocaoIdIn))
                    )
                )
            }
            return Result.success(
                CancelarAdocaoHandler(
                    solicitacaoAdocaoId = solicitacaoAdocaoId,
                )
            )
        }
    }
}
/**
 * Cria um objeto Problem representando um erro ao tentar cancelar uma adoção.
 *
 * @param detail Detalhes sobre o erro.
 * @param extra Informações adicionais sobre o erro.
 * @return Um objeto Problem com o erro especificado.
 */
private fun adocaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel cancelar a adoção.",
    detail = detail,
    type = URI("/cancelar-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)