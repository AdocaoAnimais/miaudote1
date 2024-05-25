package com.projeto2.miaudote.apresentation.controllers

import com.projeto2.miaudote.apresentation.Request.LoginRequest
import com.projeto2.miaudote.application.handler.ProcessorHandler
import com.projeto2.miaudote.application.handler.authentication.LoginHandler
import com.projeto2.miaudote.application.services.JwtService
import com.projeto2.miaudote.application.services.UsuarioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api/auth")
class AuthenticationController(
    private val service: UsuarioService,
    val serviceJwt: JwtService,
    val loginProcessor: ProcessorHandler<LoginHandler>
) {
    @PostMapping("/login")
    fun authenticate(@RequestBody login: LoginRequest): ResponseEntity<Any> {
        val request = LoginHandler.newOrProblem(login).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        val response = loginProcessor.process(request).getOrElse {
            return ResponseEntity(it, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/logged")
    fun logged(token: JwtAuthenticationToken? ): ResponseEntity<Any> {
        if(token == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(HttpStatus.OK)
    }
}