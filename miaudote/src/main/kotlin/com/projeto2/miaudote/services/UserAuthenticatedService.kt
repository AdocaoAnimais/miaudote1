package com.projeto2.miaudote.services

import com.projeto2.miaudote.repositories.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class UserAuthenticatedService(
    val repository: UsuarioRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val usuario = repository.findByUsername(username) ?: error("Usuário não encontrado.")
        return usuario.toUserAuthenticated()
    }
}