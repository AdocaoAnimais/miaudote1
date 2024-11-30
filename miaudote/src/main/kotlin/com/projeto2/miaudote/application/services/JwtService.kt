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

/**
 * Serviço responsável por gerar e codificar tokens JWT (JSON Web Tokens) para autenticação de usuários.
 * Utiliza o BCryptPasswordEncoder para codificar senhas e o JwtEncoder para codificar o token JWT.
 *
 * @param passwordEncoder O objeto BCryptPasswordEncoder utilizado para codificar senhas.
 * @param jwtEncoder O objeto JwtEncoder utilizado para gerar tokens JWT.
 */
@Service
class JwtService (
    val passwordEncoder: BCryptPasswordEncoder,
    val jwtEncoder: JwtEncoder,
) {
    /**
     * Gera um token JWT para o usuário fornecido.
     * O token contém informações sobre o usuário, como seu ID, além de um tempo de expiração.
     * O token gerado é utilizado para autenticação e autorização em sistemas que utilizam JWT.
     *
     * @param usuario O usuário para o qual o token JWT será gerado.
     * @return O token JWT gerado, como uma string.
     */
    fun generateToken(usuario: Usuario): String {
        val now = Instant.now()
        val expiraEm = 3600L
        val claims =
            JwtClaimsSet.builder().issuer("backend")
                .subject(usuario.id.toString())
                .expiresAt(now.plusSeconds(expiraEm))
                .issuedAt(now).build()

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }
}