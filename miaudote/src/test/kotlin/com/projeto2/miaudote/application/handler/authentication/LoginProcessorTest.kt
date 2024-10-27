package com.projeto2.miaudote.application.handler.authentication

import com.projeto2.miaudote.BaseTestConfig
import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.apresentation.Request.LoginRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.net.URI


class LoginProcessorTest : BaseTestConfig() {

    @Autowired
    lateinit var processor: ProcessorHandler<LoginHandler>

    @Test
    @DisplayName("Usuário não encontrado na base de dados")
    fun `usuario_nao_encontrado`() {
        val login = LoginRequest(
            username = "testes",
            senha = "senha3123"
        )
        val result = LoginHandler.newOrProblem(login)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Usuario com username 'testes' não encontrado")
            assertEquals(problem.title, "Não foi possivel efetuar login")
            assertEquals(problem.type, URI("/login"))
        }
    }

    @Test
    @DisplayName("Login com senha incorreta")
    fun `senha_incorreta`() {
        val login = LoginRequest(
            username = "paulina",
            senha = "senha3123"
        )
        val result = LoginHandler.newOrProblem(login)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "A senha informada está incorreta")
            assertEquals(problem.title, "Não foi possivel efetuar login")
            assertEquals(problem.type, URI("/login"))
        }
    }

    @Test
    @DisplayName("login sem username")
    fun `login_sem_username`() {
        val login = LoginRequest(
            username = null,
            senha = "senha3123"
        )
        val result = LoginHandler.newOrProblem(login)
        assertEquals(result.isFailure, true)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Campo 'username' não pode ser null ou vazio")
            assertEquals(problem.title, "Não foi possivel efetuar login")
            assertEquals(problem.type, URI("/login"))
        }
    }

    @Test
    @DisplayName("login sem senha")
    fun `login_sem_senha`() {
        val login = LoginRequest(
            username = "username",
            senha = null
        )
        val result = LoginHandler.newOrProblem(login)
        assertEquals(result.isFailure, true)
        val request = result.getOrElse { return }
        val response = processor.process(request).getOrElse { it ->
            val problem = it as Problem
            assertEquals(problem::class.java, Problem::class.java)
            assertEquals(problem.detail, "Campo 'senha' não pode ser null ou vazio")
            assertEquals(problem.title, "Não foi possivel efetuar login")
            assertEquals(problem.type, URI("/login"))
        }
    }

    @Test
    @DisplayName("login com sucesso")
    fun `login_com_sucesso`() {
        val login = LoginRequest(
            username = "patolino",
            senha = "patolino"
        )
        val result = LoginHandler.newOrProblem(login)
        assertEquals(result.isFailure, false)
        val request = result.getOrElse { return }
        val response = processor.process(request)
        assertEquals(response.isFailure, false)
    }
}