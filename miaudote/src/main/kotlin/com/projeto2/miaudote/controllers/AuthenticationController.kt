package com.projeto2.miaudote.controllers

import com.projeto2.miaudote.controllers.Adapters.Request.LoginRequest
import com.projeto2.miaudote.controllers.Adapters.Response.LoginResponse
import com.projeto2.miaudote.services.JwtService
import com.projeto2.miaudote.services.UsuarioService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant


@RestController
@RequestMapping("api/auth")
class AuthenticationController(
    private val service: UsuarioService,
    val serviceJwt: JwtService
) {
    @PostMapping("/login")
    fun authenticate(@RequestBody login: LoginRequest): ResponseEntity<LoginResponse> {
        val usuario = service.obterUsername(login.username) ?: return ResponseEntity.notFound().build()
        if (!usuario.validaLogin(login, serviceJwt.passwordEncoder)) throw BadCredentialsException("Senha incorreta.")
        val expiraEm = 300L
        val jwtToken = serviceJwt.generateToken(usuario)
        val response = LoginResponse(
            username = usuario.username,
            accessToken = jwtToken,
            expiresIn = expiraEm)
        return ResponseEntity(response, HttpStatus.OK)
    }
}