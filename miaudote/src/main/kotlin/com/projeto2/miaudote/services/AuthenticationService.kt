package com.projeto2.miaudote.services

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val jwtService: JwtService,
) {
    fun authenticate(auth: Authentication): String {
        return jwtService.generateToken(auth)
    }
}