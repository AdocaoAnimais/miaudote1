package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.domain.entities.Endereco
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI
/**
 * Serviço responsável por obter os dados de endereço a partir de um CEP utilizando a API ViaCep.
 * Faz uso de um RestTemplate para realizar requisições HTTP à API ViaCep.
 *
 * @param restTemplate O objeto RestTemplate utilizado para realizar as requisições HTTP.
 * @param urlApiCep A URL base da API ViaCep configurada no arquivo de propriedades.
 * @param requestType O tipo de requisição (geralmente sufixo como "/json") configurado no arquivo de propriedades.
 */
@Service
class ViaCepService(
    val restTemplate: RestTemplate,
    @Value("\${viacep.url}")
    private val urlApiCep: String,
    @Value("\${viacep.requestType}")
    private val requestType: String
) {
    /**
     * Obtém os dados de endereço a partir do CEP fornecido, fazendo uma requisição à API ViaCep.
     * Caso o CEP seja válido, retorna um objeto `Endereco` com os dados correspondentes.
     * Caso contrário, retorna uma falha com o erro apropriado.
     *
     * @param cep O CEP do qual se deseja obter os dados de endereço.
     * @return Um objeto `Result` que contém os dados de endereço em caso de sucesso ou um erro em caso de falha.
     */
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