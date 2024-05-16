package com.projeto2.miaudote.security

import com.projeto2.miaudote.entities.Usuario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserAuthenticated(
    private val usuario: Usuario,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return usuario.senha
    }

    override fun getUsername(): String {
        return usuario.username
    }

    override fun isAccountNonExpired(): Boolean {
        return false;
    }

    override fun isAccountNonLocked(): Boolean {
        return false
        return !usuario.emailVerificado
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
        return !usuario.emailVerificado
    }
}