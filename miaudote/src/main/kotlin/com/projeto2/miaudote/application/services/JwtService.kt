package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.Usuario
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors


@Service
class JwtService (
    val passwordEncoder: BCryptPasswordEncoder,
    val jwtEncoder: JwtEncoder,
) {

    fun generateToken(usuario: Usuario): String {
        val now = Instant.now()
        val expiraEm = 300L
        val claims =
            JwtClaimsSet.builder().issuer("backend")
                .subject(usuario.id.toString())
                .expiresAt(now.plusSeconds(expiraEm))
                .issuedAt(now).build()

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }
}