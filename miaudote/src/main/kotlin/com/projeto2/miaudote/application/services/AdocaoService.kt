package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.domain.entities.Adocao
import com.projeto2.miaudote.domain.entities.SolicitacaoAdocao
import com.projeto2.miaudote.infraestructure.repositories.AdocaoRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.URI
import java.util.UUID

@Service
class AdocaoService(
    private val respository: AdocaoRepository,
) {
    fun obterPorPetId(petId: Long): Adocao?{
        return respository.findByPetId(petId)
    }

    fun obterPorSolicitacaoId(solicitacaoId: UUID): Adocao? {
        return respository.findBySolicitacaoId(solicitacaoId)
    }

    fun criar(adocao: Adocao): Adocao{
        return respository.save(adocao)
    }

    fun deletar(adocao: Adocao) {
        respository.delete(adocao)
    }
}
fun Adocao?.toProblem(): Result<Adocao> {
    if (this != null) return Result.success(this)
    return Result.failure(
        Problem(
            title = "Adoção não encontrada",
            detail = "Não existe adoção para os parametros informados.",
            type = URI("/obter-adoção"),
            status = HttpStatus.BAD_REQUEST,
            extra = null
        )
    )
}