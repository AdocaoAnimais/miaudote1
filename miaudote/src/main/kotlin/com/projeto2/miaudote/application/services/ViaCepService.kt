package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.domain.entities.Endereco
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class ViaCepService(
    val restTemplate: RestTemplate,
    @Value("\${viacep.url}")
    private val urlApiCep: String,
    @Value("\${viacep.requestType}")
    private val requestType: String
) {
    fun getDataFromCep(cep: String): Result<Endereco?> {
        return try {
            val urlMontada = urlApiCep + cep + requestType
            val response = restTemplate.getForObject(
                urlMontada,
                Endereco::class.java
            )
            Result.success(response)
        } catch (e: HttpClientErrorException) {
            Result.failure(
                Problem(
                    "Não foi possível obter os dados do CEP",
                    "CEP inválido.",
                    e.statusCode.value(),
                    URI("/viacep-service/getDataFromCep"),
                    null
                )
            )
        } catch (e: Exception) {
            Result.failure(
                Problem(
                    "Não foi possível obter os dados do CEP",
                    "CEP inválido.",
                    HttpStatus.BAD_REQUEST,
                    URI("/viacep-service/getDataFromCep"),
                    null
                )
            )
        }
    }
}