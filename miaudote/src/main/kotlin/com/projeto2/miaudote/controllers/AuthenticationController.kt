package com.projeto2.miaudote.controllers

import com.projeto2.miaudote.services.AuthenticationService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/")
class AuthenticationController(
    private val authenticationService: AuthenticationService,
){
    @PostMapping("autenticar")
    fun authenticate( auth: Authentication ): String {
        return authenticationService.authenticate(auth)
    }
}