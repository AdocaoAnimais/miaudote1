package com.projeto2.miaudote.application.handler.pet

import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.RequestHandler
import com.projeto2.miaudote.application.services.PetService
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
class ObterPetsProcessor(
    private val service: PetService,
) : ProcessorHandler<ObterPetsHandler>() {
    override fun process(handler: ObterPetsHandler): Result<Any> {
        val response = when (handler.id) {
            is Long -> service.obterPetsOutrosUsuarios(handler.id)
            else -> service.obterTodosDiponiveis()
        }
        return Result.success(response) as Result<Any>
    }
}

class ObterPetsHandler private constructor(
    val id: Long?
) : RequestHandler {
    companion object {
        fun newOrProblem(token: JwtAuthenticationToken?): Result<ObterPetsHandler> {
            val id = token?.name?.toLongOrNull()

            return Result.success(ObterPetsHandler(id))
        }
    }
}