package com.projeto2.miaudote.application.handler.solicitacaoAdocao

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.application.services.*
import com.projeto2.miaudote.apresentation.Response.SolicitarAdocaoResponse
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.domain.entities.toProblem
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.net.URI
import java.time.LocalDateTime
/**
 * Processador responsável por realizar a solicitação de adoção de um pet.
 *
 * @property emailService Serviço para enviar e-mails.
 * @property service Serviço para gerenciar solicitações de adoção.
 * @property usuarioService Serviço para gerenciar operações relacionadas a usuários.
 * @property petService Serviço para gerenciar operações relacionadas a pets.
 * @property adocaoService Serviço para gerenciar adoções.
 * @property baseUrl URL base da aplicação, utilizada para gerar links de confirmação e cancelamento.
 */
@Component
class SolicitarAdocaoProcessor(
    val emailService: EmailService,
    val service: SolicitacaoAdocaoService,
    val usuarioService: UsuarioService,
    val petService: PetService,
    val adocaoService: AdocaoService,
    @Value("\${base.url}")
    private val baseUrl: String,
) : ProcessorHandler<SolicitarAdocaoHandler>() {
    /**
     * Processa a solicitação de adoção de um pet.
     *
     * @param handler Objeto contendo os dados necessários para o processamento.
     * @return Resultado do processo, indicando sucesso ou falha.
     */
    override fun process(handler: SolicitarAdocaoHandler): Result<Any> {
        val pet = petService.obterPorId(handler.petId).toProblem().getOrElse { return Result.failure(it) }

        if (adocaoService.obterPorPetId(pet.id!!) != null) return Result.failure(
            solicitacaoInvalida("O pet ${pet.nome} já foi adotado.", null)
        )

        val usuarioAdotante = usuarioService.obterPorId(handler.idUsuario).toProblem()
            .getOrElse { return Result.failure(it) }

        if (!usuarioAdotante.emailVerificado) return Result.failure(
            solicitacaoInvalida(
                "Adoção permitida apenas para usuários com email verificado, verifique seu email!",
                null
            )
        )

        if (service.obterPorAdotanteIdPetId(usuarioAdotante.id!!, pet.id) != null) return Result.failure(
            solicitacaoInvalida("O usuário já possui solicitação pendente para o pet ${pet.nome}.", null)
        )

        val usuarioResponsavel = usuarioService.obterPorId(pet.idUsuario).toProblem()
            .getOrElse { return Result.failure(it) }

        if (usuarioAdotante.id == usuarioResponsavel.id) return Result.failure(
            solicitacaoInvalida("O usuário não pode solicitar a adoção de seu próprio pet.", null)
        )
        val solicitacao = SolicitacaoAdocao(
            id = null,
            petId = pet.id,
            usuarioAdotante = usuarioAdotante.id!!,
            usuarioResponsavel = usuarioResponsavel.id!!,
            dataSolicitacao = LocalDateTime.now(),
            dataConfirmacaoUserAdotante = null,
            dataConfirmacaoUserResponsavel = null,
        )

        val result = service.criar(solicitacaoAdocao = solicitacao)

        val linkConfirmacao =
            "$baseUrl/solicitacao-adocao/confirmar-solicitacao/${result.id}"
        val linkCancelamento =
            "$baseUrl/solicitacao-adocao/cancelar-solicitacao/${result.id}"

        emailService.enviarEmailUsuarioResponsavel(
            responsavel = usuarioResponsavel,
            pet = pet,
            linkConfirmacaoSolicitacao = linkConfirmacao,
            linkCancelaSolicitacao = linkCancelamento,
            adotante = usuarioAdotante,
        )

        emailService.enviarEmail(
            to = usuarioAdotante.email,
            subject = "[MIAUDOTE] Solicitação de Adoção",
            conteudo = geraConfirmacao(pet.nome)
        )

        val response = SolicitarAdocaoResponse(
            id = result.petId,
            response = geraConfirmacao(pet.nome)
        )
        return Result.success(response)
    }

    /**
     * Gera uma mensagem de confirmação para o adotante.
     *
     * @param nomePet Nome do pet solicitado.
     * @return Mensagem de confirmação formatada.
     */
    private fun geraConfirmacao(nomePet: String): String {
        val confirmacao = """
            Solicitação de adoção realizada com sucesso!! 
            Já enviamos um email ao responsável pelo(a) $nomePet informando seu:
            nome, email e contato cadastrados aqui no Miaudote.
            Para que ele possa entrar em contato com você e dar continualidade a adoção!
        """.trimIndent()
        return confirmacao
    }
}

/**
 * Handler para solicitar a adoção de um pet, contendo o ID do usuário e do pet.
 *
 * @property idUsuario ID do usuário que está solicitando a adoção.
 * @property petId ID do pet que está sendo solicitado para adoção.
 */
class SolicitarAdocaoHandler private constructor(
    val idUsuario: Long,
    val petId: Long,
) : RequestHandler {
    companion object {
        /**
         * Cria uma nova instância do handler ou retorna um erro caso o ID do pet ou do usuário seja inválido.
         *
         * @param petId ID do pet em formato String.
         * @param token Token de autenticação JWT do usuário.
         * @return Resultado com a instância do handler ou um erro.
         */
        fun newOrProblem(petId: String?, token: JwtAuthenticationToken): Result<SolicitarAdocaoHandler> {
            val pedIdIn: Long = petId?.toLongOrNull() ?: return Result.failure(
                solicitacaoInvalida("Id do pet inválido.", null)
            )

            val id = token.name.toLongOrNull() ?: return Result.failure(
                solicitacaoInvalida("Id do usuario é inválido.", null)
            )
            return Result.success(
                SolicitarAdocaoHandler(
                    idUsuario = id,
                    petId = pedIdIn
                )
            )
        }
    }
}
/**
 * Cria um objeto Problem representando um erro ao tentar solicitar a adoção.
 *
 * @param detail Detalhes sobre o erro.
 * @param extra Informações adicionais sobre o erro.
 * @return Um objeto Problem com o erro especificado.
 */
private fun solicitacaoInvalida(detail: String, extra: Map<String, String?>?): Problem = Problem(
    title = "Não foi possivel solicitar a adoção.",
    detail = detail,
    type = URI("/solicitacao-adocao"),
    status = HttpStatus.BAD_REQUEST,
    extra = extra
)